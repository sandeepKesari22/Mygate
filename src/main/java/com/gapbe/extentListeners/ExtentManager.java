package com.gapbe.extentListeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {
  private static ExtentReports extent;

  public static ExtentReports createInstance(String filename) {
    ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(filename);
    htmlReporter.config().setEncoding("utf-8");
    htmlReporter.config().setDocumentTitle("W2A automation Reports");
    htmlReporter.config().setReportName("Automation Test Results");
    htmlReporter.config().setTheme(Theme.DARK);
    extent = new ExtentReports();
    extent.attachReporter(htmlReporter);
    extent.setSystemInfo("Organization", "MyGate");
    extent.setSystemInfo("Vertical", "GuardApp");
    return extent;
  }
}
