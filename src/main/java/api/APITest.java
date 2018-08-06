package api;

import api.beans.TestDataBean;
import api.utils.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.TestException;
import org.testng.annotations.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class APITest {
    private List<TestDataBean> testData = new ArrayList<>();
    public static HttpClient client;

    @Parameters({"excelPath", "sheet"})
    @BeforeTest
    public void init(@Optional("") String excelPath, @Optional("") String sheet) {
        testData = ExcelUtil.readTestDataFromExcel(TestDataBean.class, excelPath, sheet);
    }

    @DataProvider(name="test_data")
    protected Iterator<Object[]> testDataProvider(ITestContext context) {
        List<Object[]> dataProvider = new ArrayList<Object[]>();
        for (TestDataBean data : testData) {
            if (data.isRun()) {
                dataProvider.add(new Object[] { data });
            }
        }
        return dataProvider.iterator();
    }

    @Test(dataProvider = "test_data")
    public void apiTest(TestDataBean testDataBean) throws Exception {
        String url = testDataBean.getHost() + testDataBean.getSuffix();
        HttpUriRequest request = HttpUtil.createRequest(testDataBean, url);
        String responseData;
        try {
            HttpResponse response = client.execute(request);
            int responseStatus = response.getStatusLine().getStatusCode();
            if (!StringUtil.isEmptyString(testDataBean.getStatus() + "")) {
                Assert.assertEquals(responseStatus, testDataBean.getStatus(), "response status is not correct");
            } else {
                if (200 > responseStatus || responseStatus >= 300) {
                    throw new TestException("incorrect response status：" + responseStatus);
                }
            }

            HttpEntity respEntity = response.getEntity();
            responseData= EntityUtils.toString(respEntity, "UTF-8");
        } catch (Exception e) {
            throw e;
        } finally {
            request.abort();
        }

        ReportUtil.log("response info:" + responseData);

        ResultCheckUtil.verifyResult(responseData, testDataBean.getVerifyField());
        ReportUtil.log(testDataBean.getCaseName() + " test done.");
    }
}
