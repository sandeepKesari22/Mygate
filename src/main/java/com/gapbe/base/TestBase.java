package com.gapbe.base;

import com.qautils.commonutils.HttpUtility;
import lombok.extern.log4j.Log4j2;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.reporters.EmailableReporter2;
import java.lang.reflect.Method;
import java.util.Arrays;

@Log4j2
public class TestBase extends EmailableReporter2 {
  public static HttpUtility httpUtility;
  public static String name;
  public static String mobileNumber;
  public static String societyName;
  public static String flatNo;

  @BeforeSuite
  public void setUp() {
    log.info("===========GAP backend setup initializing.==========");

    this.httpUtility = HttpUtility.getHttpUtility();
    name = System.getProperty("name");
    mobileNumber = System.getProperty("mobile");
    societyName = System.getProperty("society");
    flatNo = System.getProperty("flat");
  }

  @BeforeMethod
  public void beforeMethod(final Method method, Object[] testData, ITestContext ctx) {
    ThreadLocal<String> testName = new ThreadLocal<>();
    if (testData.length > 0) {
      testName.set(method.getName() + "_" + testData[0]);
      ctx.setAttribute("testName", testName.get());
    } else
      ctx.setAttribute("testName", method.getName());
    log.info(
        "================================= Executing test: {} =================================",
        method.getName());
  }

  @AfterMethod
  public void afterMethod(final Method method, final ITestResult iTestResult) {
    log.info(
        "================================= Test {} ended with status: {} and parameters {} =================================",
        method.getName(),
    iTestResult.getStatus() == 1 ? "SUCCESS" : "FAILURE",
            Arrays.asList(iTestResult.getParameters()));
  }

  @AfterSuite
  public void tearDown() {
    log.info("===========GAP backend tearingdown.==========");
  }
}
