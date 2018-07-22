package justinb99.sampleapi.service.resource;

import justinb99.sampleapi.engine.model.Rate;
import justinb99.sampleapi.engine.service.RateService;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/")
public class RateResource {

  private static final String START = "start";
  private static final String END = "end";

  private final RateService rateService;

  @Inject
  public RateResource(RateService rateService) {
    this.rateService = rateService;
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
    return getRateCommon(start, end);
  }

  @Path("rate.xml")
  @GET
  @Produces(MediaType.APPLICATION_XML)
  public Rate getRateXml(@QueryParam(START) String start, @QueryParam(END) String end) {
    return getRateCommon(start, end);
  }

  private Rate getRateCommon(String start, String end) {
    return rateService.getRate(start, end);
  }

}
