package demo;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import java.net.URI;


object Main extends App {
    // Base URI the Grizzly HTTP server will listen on
    val BASE_URI = "http://localhost:8080/myapp/";
    
    def startServer(): HttpServer = {
        // create a resource config that scans for JAX-RS resources and providers
        // in com.example package
        val rc = new ResourceConfig().packages("demo")

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc)
    }

    
    val server = startServer();
    println(s"Jersey app started with WADL available at ${BASE_URI}sapplication.wadl Hit enter to stop it...")
    readLine()
    server.shutdownNow()
    
}

