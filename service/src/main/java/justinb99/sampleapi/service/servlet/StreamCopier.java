package justinb99.sampleapi.service.servlet;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamCopier {

  public void copy(InputStream from, OutputStream to) throws IOException {
    IOUtils.copy(from, to);
  }

}
