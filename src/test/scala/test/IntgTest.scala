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
import test.utils.FileUtils
import demo.DemoApp
import javax.ws.rs.client.ClientBuilder
import demo.dto.C1Result
import javax.ws.rs.client.WebTarget
import demo.dto.C2Params
import demo.dto.C2Params
import demo.dto.C2Result
import javax.ws.rs.client.Entity


class IntgTest extends FlatSpec with Matchers {

  
  "App" should "responds correctly on GET requests" in {
    val f2data = Seq(0d,1d,2d,4.56d,10d,11d,12.34d)
    val f2rez  = Seq(0d,1d,2d,4.56d,10d,1d,2.34d)
    
    val file = FileUtils.testFile(content = f2data.mkString(","))
    val app = new DemoApp(f2Path = file.getPath)
    
    val client = ClientBuilder.newClient()
    val target = client.target(app.baseUrl)
    
    try {
      f2rez.zipWithIndex.foreach { case (v,i) => 
        performGet(target)(i) shouldBe v
      }
    } finally {
      app.stop
    }
  }
  
  
  it should "reply correct on POSTed data" in {
    val f1data = Seq(1d,8d)
    val f1file = FileUtils.testFile(content = f1data.mkString(","))
    
    val app = new DemoApp(f1Path = f1file.getPath)
    val client = ClientBuilder.newClient()
    val target = client.target(app.baseUrl)
        
    try {
      performPost(target)(C2Params(5, 0, 0)) shouldBe 0
      performPost(target)(C2Params(15, 0, 0)) shouldBe 1
      performPost(target)(C2Params(5, 1, 1)) shouldBe 1
    } finally {
      app.stop
    }
  }
  
  
  it should "save POSTed data" in {
    val f1data = Seq(1d,8d)
    val f1file = FileUtils.testFile(content = f1data.mkString(","))
    
    val app = new DemoApp(f1Path = f1file.getPath)
    val client = ClientBuilder.newClient()
    val target = client.target(app.baseUrl)
    try {
      performPost(target)(C2Params(5, 0, 0))
      app.f2dao.read(0) shouldBe Some(16d)
      
      performPost(target)(C2Params(10, 0, 0))
      app.f2dao.read(0) shouldBe Some(11d)
      
      performPost(target)(C2Params(5, 1, 1))
      app.f2dao.read(1) shouldBe Some(13d)
    } finally {
      app.stop
    }
  }
  
  
  def performGet(target: WebTarget)(v2: Int): Double = {
    val responseObj = target.path("demo").path(v2.toString).request().get(classOf[C1Result])
    responseObj.value
  }
  
  
  def performPost(target: WebTarget)(params: C2Params): Int = {
    val responseObj = target.path("demo").request().post(Entity.entity(params, "application/xml"),classOf[C2Result])
    responseObj.value
  }
}