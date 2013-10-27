package in.vagmim.autoloan.api;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Assert;

import static org.junit.Assert.assertEquals;
import in.vagmim.autoloan.models.Account;
import in.vagmim.autoloan.models.Customer;
import in.vagmim.autoloan.models.Loan;
import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class AutoLoanStepDefs {
	Customer customer;
	Loan currentLoan;
	@Given("^today is \"([^\"]*)\"$")
	public void today_is(String arg1) throws Throwable {
	    // Express the Regexp above with the code you wish you had
//	    throw new PendingException();
	}

	@Given("^I have logged in as a Chase user$")
	public void I_have_logged_in_as_a_Chase_user() throws Throwable {
	    this.customer = new Customer();
	}

	@Given("^I have a checking account with the following details$")
	public void I_have_a_checking_account_with_the_following_details(List<List<String>> account) throws Throwable {
		// |account_id | 12345 |
		// |principal_balance | $ 23000 |
		// [ ["account_id", "12345"] ["principal_balance", "$ 23000"] ]
		Account accountObj = new Account();
		for(List<String> kv : account){
			setAccountDetail(kv.get(0),kv.get(1),accountObj);
		}
		this.customer.getAccounts().add(accountObj);
	}
	private void setAccountDetail(String key, String value, Account account) {
		if(key.equals("account_id"))
			account.setAccountNumber(value);
		if(key.equals("available_balance")) {
			account.setAvailableBalance(parseDollar(value));
		}
	}

	@Given("^I have a Chase User with Auto loan$")
	public void I_have_a_Chase_User_with_Auto_loan(List<List<String>> loan) throws Throwable {
	    Loan loanObj = new Loan();
	    for(List<String> kv : loan) {
	    	setLoanDetail(kv.get(0), kv.get(1),loanObj);
	    }
	    this.customer.getLoans().add(loanObj);
	    
	}
	private void setLoanDetail(String key, String value, Loan loan) {
		if(key.equals("loan_account_number")) {
			loan.setLoanAccountNumber(value);
		}
		if(key.equals("principal_balance")) {
			loan.setPrincipalBalance(parseDollar(value));
		}
		if(key.equals("amount_due")) {
			loan.setAmountDue(parseDollar(value));
		}
		if(key.equals("next_payment_date")) {
			loan.setNextPaymentDate(parseDate(value));
		}
		if(key.equals("late_fee")) {
			loan.setLateFee(parseDollar(value));
		}
		if(key.equals("additional_interest")) {
			loan.setAdditionalInterest(parseDollar(value));
		}
	}
	private Double parseDollar(String value) {
		return Double.parseDouble(value.replace("$", "").trim());
	}

	private Date parseDate(String value) {
		return DateTime.parse(value,
				DateTimeFormat.forPattern("dd-MMM-yyyy")).toDate();
	}

	@When("^I make a payment of \\$ (\\d+) against the loan (\\d+)$")
	public void I_make_a_payment_of_$_against_the_loan(int amount, Integer loanNumber) throws Throwable {
	    customer.payForLoan(amount, loanNumber.toString());
	}

	@Then("^my account balance should be \\$ (\\d+)$")
	public void my_account_balance_should_be_$(int newBalance) throws Throwable {
		Assert.assertEquals(customer.getDefaultAccount().getAvailableBalance(), new Double(newBalance));
	}

	@When("^I view my payment activity for loan (\\d+)$")
	public void I_view_my_payment_activity_for_loan(Integer loanNumber) throws Throwable {
	   currentLoan = customer.getLoanFor(loanNumber.toString()); 
	}

	@Then("^I should see the following details$")
	public void I_should_see_the_following_details(List<List<String>> table) throws Throwable {
	    Map<String,String> properties = tableToMap(table);
	    Assert.assertEquals(properties.get("principal_balance"), dollarValue(this.currentLoan.getPrincipalBalance()));
	    Assert.assertEquals(properties.get("amount_due"), dollarValue(this.currentLoan.getAmountDue()));
	    Assert.assertEquals(parseDate(properties.get("next_payment_date")), this.currentLoan.getNextPaymentDate());   
	}
	private String dollarValue(Double value) {
		return "$ " + value.intValue();
	}
	
	private Map<String,String> tableToMap(List<List<String>> table) {
		Map<String,String> props = new HashMap<String,String>();
		for(List<String> kv : table) {
			props.put(kv.get(0), kv.get(1));
		}
		return props;
	}
}
