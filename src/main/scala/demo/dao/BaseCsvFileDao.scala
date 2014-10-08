package demo.dao

import scala.collection.mutable
import scala.util.control.NonFatal
import java.io.File
import java.util.concurrent.locks.ReentrantReadWriteLock
import java.util.ArrayList
import scala.collection.JavaConversions._


class BaseCsvFileDao(fileName: String) {
    
  val data = new ArrayList[Option[Double]](100)
  val rwl = new ReentrantReadWriteLock()
  
  loadData()
  

  def read(index: Int): Option[Double] = {
    rwl.readLock().lock()
    val rez = if (index < data.size()) {
      data.get(index)
    } else {
      None
    }
    rwl.readLock().unlock()    
    rez
  }
  
  
  def write(index: Int, value: Double): Unit = {
    rwl.writeLock().lock()
    if (index >= data.size()) {
      (data.size() to index + 1).foreach { in => data.add(None)}
    }
    data.set(index, Some(value))
    rwl.writeLock().unlock()
    saveFile()
  }
  
  def readAll(): Seq[Option[Double]] = {
    rwl.readLock().lock()
    try {
      Seq() ++ data
    } finally {
      rwl.readLock().unlock()
    }
  }
  
  private[this] def loadData(): Unit = {
    rwl.writeLock().lock()
    val f = new java.io.File(fileName)
    if (f.exists()) {
      val s = scala.io.Source.fromFile(f)
      try {
        while (s.hasNext) {
          val raw = s.takeWhile(_ != ',').mkString
          val v = try {
            Some(raw.toDouble)
          } catch {
            case NonFatal(ex) => None
          }
          data.add(v)
        }
      } finally {
        rwl.writeLock().unlock()
        s.close()
      }
    }
  }
  
  
  private[this] def saveFile(): Unit = {
    rwl.readLock().lock()
    var first = true
    try {
      val f = new File(fileName)
      printToFile(f){ pw =>
        data.foreach { opt =>
          if (!first) {
            pw.print(",")
          } else {
            first = false
          }
          opt.map(v => pw.print(v))
        }  
      }    
    } finally {
      rwl.readLock().unlock()
    }
  }
  
  
  private[this] def printToFile(f: java.io.File)(op: java.io.PrintWriter => Unit) {
    val p = new java.io.PrintWriter(f)
    try { op(p) } finally { p.close() }
  }
}