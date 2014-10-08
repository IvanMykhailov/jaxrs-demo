package test.utils

import java.io.File
import scala.util.Random

object FileUtils {
  
  def testFile() = {
    val f = new File(s"/tmp/jaxwsdemo/test/test_${Math.abs(Random.nextInt)}.csv")
    f.delete()
    f
  }
  
  def testFile(content: String) = {
    val f = new File(s"/tmp/jaxwsdemo/test/test_${Math.abs(Random.nextInt)}.csv")
    f.delete()
    val parent = f.getParentFile()
    if(!parent.exists && !parent.mkdirs) {
      throw new IllegalStateException("Couldn't create dir: " + parent)
    }
    printToFile(f) { _.print(content)}
    f
  }
  
  private[this] def printToFile(f: java.io.File)(op: java.io.PrintWriter => Unit) {
    val p = new java.io.PrintWriter(f)
    try { op(p) } finally { p.close() }
  }
}