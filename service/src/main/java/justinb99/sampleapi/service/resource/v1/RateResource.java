package justinb99.sampleapi.service.resource.v1;

import justinb99.sampleapi.schema.RateOuterClass.Rate;
import justinb99.sampleapi.engine.service.RateEngine;
import justinb99.sampleapi.schema.XmlRate;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/v1")
public class RateResource {

  private static final String START = "start";
  private static final String END = "end";

  private final RateEngine rateEngine;

  @Inject
  public RateResource(RateEngine rateEngine) {
    this.rateEngine = rateEngine;
  }

  @Path("rate")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Rate getRate(@QueryParam(START) String start, @QueryParam(END) String end) {
    return getRateJson(start, end);
  }

  @Path("rate.json")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Rate getRateJson(@QueryParam(START) String start, @QueryParam(END) String end) {
    return rateEngine.getRate(start, end);
  }

  @Path("rate.xml")
  @GET
  @Produces(MediaType.APPLICATION_XML)
  public XmlRate getRateXml(@QueryParam(START) String start, @QueryParam(END) String end) {
    return XmlRate.of(rateEngine.getRate(start, end));
  }

  @Path("rate.proto")
  @GET
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  public byte[] getRateProtoBinary(@QueryParam(START) String start, @QueryParam(END) String end) {
    return rateEngine.getRate(start, end).toByteArray();
  }

}
