package com.gapbe.commons.Utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.Assert;

public class CommonUtils {

  public JSONObject getJsonObjectViaEntity(HttpResponse response) throws Exception {
    /*Since the passed Entity was not repeatable, I was getting error "Stream closed"
    hence divided teh method into two*/
    BufferedHttpEntity myBufferedEntity = new BufferedHttpEntity(response.getEntity());
    JSONParser parser = new JSONParser();
    JSONObject output = (JSONObject) parser.parse(EntityUtils.toString(myBufferedEntity));
    if(output.toString().equals("{\"message\":\"Multiple devices login disabled\",\"es\":\"1\"}")){
      Assert.fail("message:Multiple devices login disabled");
    }
    return output;
  }

  public String getKeyValueViaJsonObject(JSONObject json, String keyValue) {
    String value = json.get(keyValue).toString();
    return value;
  }
}
