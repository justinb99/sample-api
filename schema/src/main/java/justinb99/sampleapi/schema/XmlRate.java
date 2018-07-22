package justinb99.sampleapi.schema;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import justinb99.sampleapi.schema.RateOuterClass.Rate;

import static justinb99.sampleapi.schema.RateOuterClass.Rate.PRICE_FIELD_NUMBER;
import static justinb99.sampleapi.schema.RateOuterClass.Rate.STATUS_FIELD_NUMBER;

@XmlRootElement(name = "rate")
public class XmlRate {

  private Rate rate;

  //Default constructor required for XML Serialization, but doesn't need to be public
  XmlRate() {
  }

  public XmlRate(Rate rate) {
    this.rate = rate;
  }

  @XmlElement
  public Integer getPrice() {
//    int price = rate.getPrice();
//    System.out.println("\n\n\n****price = " + price);
//    return price;
    if (rate.getPriceOrStatusCase().getNumber() == PRICE_FIELD_NUMBER)
      return rate.getPrice();
    else
      return null;
  }

  @XmlElement
  public Rate.Status getStatus() {
//    Rate.Status status = rate.getStatus();
//    System.out.println("\n\n\n****status = " + status);
//    return status;
    if (rate.getPriceOrStatusCase().getNumber() == STATUS_FIELD_NUMBER)
      return rate.getStatus();
    else
      return null;
  }

  public static XmlRate of(Rate rate) {
    return new XmlRate(rate);
  }

}
