package api.utils;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import org.testng.Assert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResultCheckUtil {
    public static void verifyResult(String responseData, String verifyField) {
        if (StringUtil.isEmptyString(verifyField))
            return;
        ReportUtil.log("to be verify\r\n：" + verifyField);
        String[] allFields = verifyField.replaceAll("\r\n", "").split(";");
        String operator = "=";
        String key = "", expectedValue;
        for (String field : allFields) {
            int index = field.lastIndexOf(operator);
            if ('~' == field.charAt(index - 1)) {
                operator = "~=";
            }
            if ("=".equals(operator))
                key = field.substring(0, index);
            else if ("~=".equals(operator))
                key = field.substring(0, index - 1);
            expectedValue = field.substring(index + 1);
            checkJsonValue(responseData, key, expectedValue, operator);
        }
    }

    private static void checkJsonValue(String responseData, String key, String expectedValue, String op) {
        Object document = Configuration.defaultConfiguration().jsonProvider().parse(responseData);
        Object actualValue = JsonPath.read(document, key);
        String actualValueString = actualValue.toString();
        if (actualValueString.contains("\"") || actualValueString.contains("["))
            actualValueString = actualValueString.replaceAll("\"", "").replaceAll("\\[","")
                    .replaceAll("]", "");
        if ("=".equals(op))
            Assert.assertEquals(actualValueString, expectedValue,"wrong value");
        else  if ("~=".equals(op))
            Assert.assertTrue(actualValue.toString().contains(expectedValue), "can not find: " + expectedValue + " in return value");
    }
}
