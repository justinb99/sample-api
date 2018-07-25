package justinb99.sampleapi.service.resource.v1;

import com.codahale.metrics.annotation.Timed;
import justinb99.sampleapi.engine.RateEngine;
import justinb99.sampleapi.engine.xml.XmlSerializer;
import justinb99.sampleapi.schema.RateOuterClass.Rate;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/v1")
public class RateResource {

  private static final String START = "start";
  private static final String END = "end";

  private final RateEngine rateEngine;
  private final XmlSerializer xmlSerializer;

  @Inject
  public RateResource(
    RateEngine rateEngine,
    XmlSerializer xmlSerializer
  ) {
    this.rateEngine = rateEngine;
    this.xmlSerializer = xmlSerializer;
  }

  @Path("rate")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Timed
  public Rate getRate(@QueryParam(START) String start, @QueryParam(END) String end) {
    return getRateJson(start, end);
  }

  @Path("rate.json")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Timed
  public Rate getRateJson(@QueryParam(START) String start, @QueryParam(END) String end) {
    return rateEngine.getRate(start, end);
  }

  @Path("rate.xml")
  @GET
  @Timed
  public Response getRateXml(@QueryParam(START) String start, @QueryParam(END) String end) {
    var rate = rateEngine.getRate(start, end);
    return Response
      .status(200)
      .type(MediaType.APPLICATION_XML)
      .entity(xmlSerializer.toXmlString(rate))
      .build();
  }

  @Path("rate.proto")
  @GET
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  @Timed
  public byte[] getRateProtoBinary(@QueryParam(START) String start, @QueryParam(END) String end) {
    return rateEngine.getRate(start, end).toByteArray();
  }

}
