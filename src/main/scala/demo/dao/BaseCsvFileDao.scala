package demo.dao

import scala.collection.mutable
import scala.util.control.NonFatal
import java.io.File
import java.util.concurrent.locks.ReentrantReadWriteLock
import java.util.ArrayList
import scala.collection.JavaConversions._


class BaseCsvFileDao(file: File) {
    
  /*Load all data from file to memory, since CSV format doesn't allow 
  * direct access and modification, so it is very slow.
  * Use java ArrayList as least memory consuming and optimal performance storage
  */  
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
    try {    
      if (index >= data.size()) {
        (data.size() to index).foreach { in => data.add(None)}
      }
      data.set(index, Some(value))
    } finally {
      rwl.writeLock().unlock()
    }
    /* Remove write clock to allow reading during file saving. 
     * File saving can be queued and delegated to separate thread
     * to avoid saving on each modification. */
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
  
  /*
   * loadData and saveFile methods don't use conventional scala file processing like 
   * Source.fromFile(file).mkString.split(",").map(_.toDouble)
   * 
   * I assume files are relative big and try to avoid load it to memory twice. 
   */
  private def loadData(): Unit = {    
    if (file.exists()) {
      rwl.writeLock().lock()
      val s = scala.io.Source.fromFile(file)
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
  
  
  private def saveFile(): Unit = {
    //use sync to avoid concurent file saving
    this.synchronized {
      rwl.readLock().lock()
      var first = true
      try {
        val parent = file.getParentFile()
        if(!parent.exists && !parent.mkdirs) {
          throw new IllegalStateException("Couldn't create dir: " + parent)
        }
        
        printToFile(file){ pw =>
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
  }
  
  
  private def printToFile(f: java.io.File)(op: java.io.PrintWriter => Unit) {
    val p = new java.io.PrintWriter(f)
    try { op(p) } finally { p.close() }
  }
}