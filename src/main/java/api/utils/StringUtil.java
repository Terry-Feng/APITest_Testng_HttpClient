package api.utils;

public class StringUtil {
    public static boolean isEmptyString(String str) {
        return (null == str || "".equals(str));
    }
}
