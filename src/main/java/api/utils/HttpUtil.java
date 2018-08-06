package api.utils;

import api.APITest;
import api.beans.TestDataBean;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.CoreConnectionPNames;

import java.util.ArrayList;
import java.util.List;

public class HttpUtil {
    private static final int CONNECTION_TIMEOUT = 60000;
    private static final int READ_TIMEOUT = 6000;

    public static HttpUriRequest createRequest(TestDataBean bean, String url) throws Exception {
        ReportUtil.log("test case: " + bean.getCaseName());
        ReportUtil.log("method:" + bean.getMethod());
        ReportUtil.log("url:" + url);
        if (!StringUtil.isEmptyString(bean.getBody()))
            ReportUtil.log("parameters:" + bean.getBody());
        APITest.client = new SSLUtil();//返回https请求对象
        APITest.client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT);
        APITest.client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, READ_TIMEOUT);
        String headerStr = bean.getHeader();
        Header[] header = setHeader(bean.getHeader());
        HttpEntity entity = new StringEntity(bean.getBody(), "UTF-8");

        if (bean.getMethod().toLowerCase().equals("post")) {
            HttpPost pRequest = new HttpPost(url);
            pRequest.setHeaders(header);
            pRequest.setEntity(entity);
            return pRequest;
        } else if (bean.getMethod().toLowerCase().equals("get")) {
            HttpGet gRequest = new HttpGet(url);
            gRequest.setHeaders(header);
            return gRequest;
        } else {
            //TODO add other method
        }
        return null;
    }

    private static Header[] setHeader(String headerStr) {
        if (StringUtil.isEmptyString(headerStr))
            return null;
        String key, value;
        List<Header> headers = new ArrayList<>();
        if (!StringUtil.isEmptyString(headerStr)) {
            String[] headerArr = headerStr.split(";");
            for (String h : headerArr) {
                key = h.split(":")[0];
                value = h.split(":")[1];
                Header temp = new BasicHeader(key, value);
                headers.add(temp);
            }
        }
        return headers.toArray(new Header[headers.size()]);
    }


}
