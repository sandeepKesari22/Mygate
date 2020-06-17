package com.gapbe.lib.apilib;

import com.gapbe.base.Constants;
import com.gapbe.base.TestBase;
import com.gapbe.commons.enums.RiderStatus;
import com.qautils.commonutils.HttpUtility;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpResponse;

import java.util.HashMap;
@Log4j2
public class RiderLib {
  HttpUtility httpUtility;
  String rider_url = Constants.RIDER_STATUS_UPDATE.value();
  String apiKey = Constants.ECOM_API_AUTH.value();

  public RiderLib() {
    this.httpUtility = TestBase.httpUtility;
  }

  public HttpResponse riderStatusUpdate(String passcode, String guestId, RiderStatus riderStatus)
      throws Exception {
    log.info("Marking the rider to ARRIVED state");
    String url =
        rider_url + passcode + "?guest_id=" + guestId + "&status=" + riderStatus.toString();
    HashMap<String, String> header = new HashMap<>();
    header.put("Content-Type", "application/json");
    header.put("api-key", apiKey);
    HttpResponse httpResponse = httpUtility.put(header, null, url);
    return httpResponse;
  }
}
