package api;

import api.beans.TestDataBean;
import api.utils.ExcelUtil;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class APITest {
    private List<TestDataBean> testData = new ArrayList<>();

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
    public void apiTest() {
        //TODO send request check response, create report
    }

    @AfterTest
    protected void tearDown() {
        //TODO close stream
    }
}
