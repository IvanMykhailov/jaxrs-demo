package demo;

import javax.inject.Singleton
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.WebApplicationException
import demo.dto.C1Result
import javax.ws.rs.core.Response
import demo.dto.C2Params
import javax.ws.rs.POST
import demo.dto.C2Result


@Path("demo")
class Resource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Path("{v1}")
    @Produces(Array("application/xml"))
    def c1(@PathParam("v1") v1: Integer): C1Result = {
      DemoApp.current.f2dao.read(v1) match {
        case Some(rez) if rez > 10 => C1Result(rez - 10)
        case Some(rez) => C1Result(rez)  
        case None => throw new WebApplicationException(Response.Status.NOT_FOUND);
      }
    }

    @POST
    @Produces(Array("application/xml"))
    def c2(params: C2Params): C2Result = {
      DemoApp.current.f1dao.read(params.v3) match {
        case Some(f1v3) =>
          val a = f1v3 + params.v2
          val (rez, f2toSave) = if (a < 10) {
            (0, a + 10)
          } else {
            (1, a)
          }
          DemoApp.current.f2dao.write(params.v4, f2toSave)
          C2Result(rez)
        
        case None => throw new WebApplicationException(Response.Status.NOT_FOUND);
      }      
    }
}
