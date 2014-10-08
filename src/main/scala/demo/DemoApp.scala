package demo

import org.glassfish.grizzly.http.server.HttpServer
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory
import org.glassfish.jersey.server.ResourceConfig
import java.net.URI
import demo.dao.BaseCsvFileDao
import demo.dao.BaseCsvFileDao
import java.io.File


class DemoApp(
  val baseUrl: String = "http://localhost:8080/jaxws/",
  val f1Path: String = "/tmp/jaxwsdemo/f1.csv",
  val f2Path: String = "/tmp/jaxwsdemo/f2.csv"
) {

  val f1dao = new BaseCsvFileDao(new File(f1Path))
  val f2dao = new BaseCsvFileDao(new File(f2Path))
  
  protected val rc = new ResourceConfig()
    .register(classOf[demo.Resource])
    
  val server = GrizzlyHttpServerFactory.createHttpServer(URI.create(baseUrl), rc)
  
  DemoApp.set(this)
  
  def stop(): Unit = {
    server.shutdownNow()
  }
}


/* Dirty hack to avoid full DI framework */ 
object DemoApp {
  private var app: Option[DemoApp] = None
  
  def current(): DemoApp = app.getOrElse(throw new RuntimeException("App is not initialized"))
  
  protected def set(app: DemoApp): Unit = { this.app = Some(app) }
}