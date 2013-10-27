package in.vagmim.autoloan.selenium;

import in.vagmim.autoloan.models.Loan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import static org.junit.Assert.assertEquals;
public class SeleniumStepDefs {
	SharedDriver driver;
	@Before
	public void setupSelenium(){
		driver = new SharedDriver();
	}
	@After
	public void takeSnapshot(Scenario scenario) throws InterruptedException{
		byte[] image = driver.getScreenshotAs(OutputType.BYTES);
		scenario.embed(image, "image/png");
	}
	@Given("^today is \"([^\"]*)\"$")
	public void today_is(String arg1) throws Throwable {
	 
	}

	@Given("^I have logged in as a Chase user$")
	public void I_have_logged_in_as_a_Chase_user() throws Throwable {
		driver.get("http://vagmi.github.io/mphasis-loan-html");
	}

	@Given("^I have a checking account with the following details$")
	public void I_have_a_checking_account_with_the_following_details(DataTable arg1) throws Throwable {
		
	}

	@Given("^I have a Chase User with Auto loan$")
	public void I_have_a_Chase_User_with_Auto_loan(List<List<String>> loan) throws Throwable {
		Map<String,String> fields = new HashMap<String,String>();
		fields.put("loan_account_number", "loanAccountNumber");
		fields.put("principal_balance", "principalBalance");
		fields.put("amount_due", "amountDue");
		fields.put("next_payment_date", "nextPaymentDate");
		fields.put("additional_interest", "additionalInterest");
		fields.put("late_fee", "lateFee");
		
		Map<String, String> loanMap = tableToMap(loan);
		driver.findElement(By.cssSelector("#nav-loans")).click();
		driver.findElement(By.cssSelector("button.new-loan")).click();
		WebElement formElement = driver.findElement(By.cssSelector("form#new-loan-form"));
		for(Entry<String,String> e : fields.entrySet()) {
			String mapValue = loanMap.get(e.getKey());
			if(mapValue!=null) {
				String value = parseDollar(mapValue);
				formElement.findElement(By.cssSelector("input[name="+e.getValue()+"]")).sendKeys(value);
			}
		}
		formElement.findElement(By.cssSelector("input[type=submit]")).click();
	}
	private Map<String,String> tableToMap(List<List<String>> table) {
		Map<String,String> props = new HashMap<String,String>();
		for(List<String> kv : table) {
			props.put(kv.get(0), kv.get(1));
		}
		return props;
	}
	private String parseDollar(String value) {
		return value.replace("$", "").trim();
	}

	@When("^I make a payment of \\$ (\\d+) against the loan (\\d+)$")
	public void I_make_a_payment_of_$_against_the_loan(int amount, int loanNumber) throws Throwable {
		driver.findElement(By.cssSelector("#nav-loans")).click();
		WebElement loanElement = driver.findElement(By.cssSelector("#apphost .loans .loan-"+loanNumber));
		loanElement.findElement(By.cssSelector("button.pay-loan")).click();
	}

	@Then("^my account balance should be \\$ (\\d+)$")
	public void my_account_balance_should_be_$(Integer balance) throws Throwable {
		Map<String,Integer> accts = new HashMap<String,Integer>();
		driver.findElement(By.cssSelector("#nav-accounts")).click();
		List<WebElement> accounts = driver.findElements(By.cssSelector("#apphost .accounts .account"));
		String accountNumber = null;
		for(WebElement acct : accounts) {
			accountNumber = acct.findElement(By.cssSelector("span.account_number")).getText();
			Integer accountBalance = Integer.parseInt(acct.findElement(By.cssSelector("span.account_balance")).getText());
			accts.put(accountNumber, accountBalance);
		}
		assertEquals(balance, accts.get(accountNumber));
		
	}

	@When("^I view my payment activity for loan (\\d+)$")
	public void I_view_my_payment_activity_for_loan(int arg1) throws Throwable {
	    // Express the Regexp above with the code you wish you had
	    throw new PendingException();
	}

	@Then("^I should see the following details$")
	public void I_should_see_the_following_details(DataTable arg1) throws Throwable {
	    // Express the Regexp above with the code you wish you had
	    // For automatic conversion, change DataTable to List<YourType>
	    throw new PendingException();
	}


}
