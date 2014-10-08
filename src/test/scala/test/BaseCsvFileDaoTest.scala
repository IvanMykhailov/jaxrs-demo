package test

import org.scalatest.Matchers
import org.scalatest.FlatSpec
import demo.dao.BaseCsvFileDao
import java.io.File
import scala.io.Source

class BaseCsvFileDaoTest extends FlatSpec with Matchers {
  
  val testFile = new File("/tmp/test.csv")
  
  "FileDao" should "save file correctly" in {    
    testFile.delete()
    val dao = new BaseCsvFileDao("/tmp/test.csv")
    dao.write(3,3)
    dao.write(5,5)
    dao.write(2,2)
    dao.write(6,6)
    
    Source.fromFile(testFile).mkString shouldBe ",,2.0,3.0,,5.0,6.0"
  }
  
  "FileDao" should "load file correctly" in {
    testFile.delete()
    val dao = new BaseCsvFileDao("/tmp/test.csv")
    dao.write(3,3)
    dao.write(5,5)
    dao.write(2,2)
    dao.write(6,6)
    
    val dao2 = new BaseCsvFileDao("/tmp/test.csv")
    dao2.readAll shouldBe dao.readAll
  }

}