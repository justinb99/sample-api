package justinb99.sampleapi.service.resource;

import justinb99.sampleapi.engine.model.Rate;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("rate")
public class RateResource {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Rate getRate() {
    Rate justPrice = new Rate();
    justPrice.setPrice(1500);
    return justPrice;
  }

}
