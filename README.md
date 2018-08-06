# APITest_Testng_HttpClient
This is a demo for technical assignment using <b>Java</b>, <b>TestNg</b>, <b>HttpClient</b> and <b>reportng</b> 

### To deploy the poject<br>
* Download the poject, import it to you IDE as a maven project and run `mvn clean install` with your IDE.<br>
* Download the project, navigate to your workspace, run `mvn clean install` with commandline.

### To run the test<br>
run command `mvn test`<br>
or run testng.xml with testng plugin in your IDE

### test case
To separate test data and test code, using excel manage test cases.<br>
It's direcotry is "src/main/resources/test_data.xlsx", and can be configured in testng.xml.
Each row in the excel is a test case.
* verify field in test case: use jsonpath to check result, "=" means equals and "~=" means contains.
* only "Run" field marked as "Y", the cases will be run.

### To check test report
Run with maven:<br>
`{workspace}/target/surefire-report/html/index.html`<br>
Run with Testng:<br>
`{workspace}/test-output/html/index.html`

### Main dependencies in Pom
Testng<br>
Httpclient<br>
jayway.jsonpath<br>
reportng<br>

#### please use java 8 or higher version

#### To be optimized：
  * http request
  * report format
  * test cases
