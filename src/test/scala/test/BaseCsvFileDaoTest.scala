package test

import org.scalatest.Matchers
import org.scalatest.FlatSpec
import demo.dao.BaseCsvFileDao
import java.io.File
import scala.io.Source
import scala.util.Random
import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration


class BaseCsvFileDaoTest extends FlatSpec with Matchers {
  
  def testFile() = {
    val f = new File(s"/tmp/jaxwsdemo/test/test_${Math.abs(Random.nextInt)}.csv")
    f.delete()
    f
  }
  
  "FileDao" should "save file correctly" in {
    val f = testFile()
    val dao = new BaseCsvFileDao(f)
    dao.write(3,3)
    dao.write(5,5)
    dao.write(2,2)
    dao.write(6,6)
    
    Source.fromFile(f).mkString shouldBe ",,2.0,3.0,,5.0,6.0"
  }
  
  it should "load file correctly" in {
    val f = testFile()
    val dao = new BaseCsvFileDao(f)
    dao.write(3,3)
    dao.write(5,5)
    dao.write(2,2)
    dao.write(6,6)
    
    val dao2 = new BaseCsvFileDao(f)
    dao2.readAll shouldBe dao.readAll
  }

  
  it should "work correct with concurent modification" in {
    
    val data = (0 to 1000).map { i => 
      (i, Random.nextDouble)
    }
    val f = testFile()
    val dao = new BaseCsvFileDao(f)
    
    val f1 = future {
      data.foreach { case (index, value) =>
        dao.write(index, value)
      }
    }
    val f2 = future {
      data.foreach { case (index, value) =>
        dao.write(index, value)
      }
    }
    
    Await.ready(Future.sequence(Seq(f1, f2)), Duration(1, "minute"))
        
    dao.readAll should contain theSameElementsInOrderAs data.map(e => Some(e._2))
  }
}