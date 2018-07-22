package justinb99.sampleapi.service.resource;

import justinb99.sampleapi.engine.model.Rate;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class RateResource {

  @Path("rate")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Rate getRate() {
    return getRateJson();
  }

  @Path("rate.json")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Rate getRateJson() {
    return getRateCommon();
  }

  @Path("rate.xml")
  @GET
  @Produces(MediaType.APPLICATION_XML)
  public Rate getRateXml() {
    return getRateCommon();
  }

  private Rate getRateCommon() {
    Rate justPrice = new Rate();
    justPrice.setPrice(1500);
    return justPrice;
  }

}
