package demo;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("test")
class MyResource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Path("myresource/{v1}")
    @Produces(Array("text/plain"))
    def getIt(@PathParam("v1") v1: Integer): String = {
        v1.toString();
    }

    @GET
    @Path("xml")
    @Produces(Array("application/xml"))
    def getXml(): DemoEntity = {
      DemoEntity("testName", 42)
    }
}
