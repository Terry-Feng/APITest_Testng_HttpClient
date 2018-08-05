package api;

import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class APITest {

    @BeforeTest
    public void init() {
        //TODO read test cases from excel
    }

    @DataProvider(name="test_data")
    protected Object[][] testDataProvider(ITestContext context) {
        //TODO prepare test data
        return null;
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
