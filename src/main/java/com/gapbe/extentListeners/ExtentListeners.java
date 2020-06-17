package com.gapbe.extentListeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Arrays;
import java.util.Date;

public class ExtentListeners implements ITestListener {
  static Date d = new Date();

  static String fileName = "Extent_" + d.toString().replace(":", "_") + ".html";

  private static ExtentReports extent = ExtentManager.createInstance("./Reports/" + fileName);
  public static ThreadLocal<ExtentTest> testReport = new ThreadLocal<ExtentTest>();

  public void onTestStart(ITestResult result) {
    ExtentTest test =
        extent.createTest(
            result.getTestClass().getName()
                + " : "
        + result.getTestContext().getAttribute("testName").toString());
    testReport.set(test);

  }

  public void onTestSuccess(ITestResult result) {
    String methodName = result.getMethod().getMethodName();
    String text = "<b>" + "TEST CASE:- " + methodName.toUpperCase() + " PASSED" + "</b>";
    Markup m = MarkupHelper.createLabel(text, ExtentColor.GREEN);
    testReport.get().pass(m);
  }

  public void onTestFailure(ITestResult result) {
    String exceptionMessage = Arrays.toString(result.getThrowable().getStackTrace());
    testReport
        .get()
        .fail(
            "<details"
                + "<summary>"
                + "<b>"
                + "<font color"
                + "red>"
                + "Exception Occured: Click to see"
                + "</font"
                + "</b>"
                + "</summary>"
                + exceptionMessage.replaceAll(",", "<br>")
                + "/details"
                + " \n");

    String text = "TEST CASE FAILED";
    Markup m = MarkupHelper.createLabel(text, ExtentColor.RED);
    testReport.get().log(Status.FAIL, m);
  }

  public void onTestSkipped(ITestResult result) {
    String methodName = result.getMethod().getMethodName();
    String text = "<b>" + "TEST CASE:- " + methodName.toUpperCase() + " SKIPPED" + "</b>";
    Markup m = MarkupHelper.createLabel(text, ExtentColor.ORANGE);
    testReport.get().skip(m);
  }

  public void onStart(ITestContext context) {
    System.out.println("*** Test Suite " + context.getName() + " started ***");
  }

  public void onFinish(ITestContext context) {
    if (extent != null) {
      extent.flush();
    }
  }
}
