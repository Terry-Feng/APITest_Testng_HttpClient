package api.utils;

import org.testng.Reporter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ReportUtil {
    private static String reportName = "API test report";

    private static String splitTimeAndMsg = " ===> ";
    public static void log(String msg) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("dd/MM/yyyy hh:mm:ss");
        Reporter.log(sdf.format(new Date()) + splitTimeAndMsg + msg, true);
    }

    public static String getReportName() {
        return reportName;
    }

    public static String getSpiltTimeAndMsg() {
        return splitTimeAndMsg;
    }

    public static void setReportName(String reportName) {
        if(!StringUtil.isEmptyString(reportName)){
            ReportUtil.reportName = reportName;
        }
    }
}
