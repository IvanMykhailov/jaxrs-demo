package demo;

import org.glassfish.grizzly.http.server.HttpServer
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory
import org.glassfish.jersey.server.ResourceConfig
import java.net.URI
import demo.dao.BaseCsvFileDao
import demo.dao.BaseCsvFileDao
import java.io.File


object Main extends App {
    // Base URI the Grizzly HTTP server will listen on
    val BASE_URI = "http://localhost:8080/jaxws/";
    
    val F1_Path = "/tmp/jaxwsdemo/f1.csv"
    val F2_Path = "/tmp/jaxwsdemo/f2.csv"
    
    
    def startServer(): HttpServer = {
        val rc = new ResourceConfig().packages("demo")
        GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc)
    }

    val f1dao = new BaseCsvFileDao(new File(F1_Path))
    val f2dao = new BaseCsvFileDao(new File(F2_Path))
    
    val server = startServer();
    println(s"Jersey app started with WADL available at ${BASE_URI}sapplication.wadl Hit enter to stop it...")
    readLine()
    server.shutdownNow()
    
}

