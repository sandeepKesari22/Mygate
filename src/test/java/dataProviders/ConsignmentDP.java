package dataProviders;

import com.gapbe.base.TestBase;
import com.gapbe.commons.enums.AddressMatchStatus;
import org.testng.annotations.DataProvider;

public class ConsignmentDP {

  @DataProvider
  public static Object[][] partnerList() {
    return new Object[][] {{"FLIPKART"}, {"GROFERS"}};
  }

  @DataProvider
  public static Object[][] addressCombinations() {
    return new Object[][] {
      {TestBase.mobileNumber, TestBase.societyName, AddressMatchStatus.MG_MATCH},
      {"9902532017", "Block 1 ", AddressMatchStatus.NON_MG_MATCH},
      {"9902000017", "B", AddressMatchStatus.MG_NO_MATCH}
    };
  }
}
