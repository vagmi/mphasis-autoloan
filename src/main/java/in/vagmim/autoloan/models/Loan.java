package in.vagmim.autoloan.models;

import java.util.Date;
import org.joda.time.*;

public class Loan {
	String loanAccountNumber;
	Double principalBalance;
	Double amountDue;
	Date nextPaymentDate;
	Double lateFee=0d;
	Double additionalInterest=0d;
	
	public Double getLateFee() {
		return lateFee;
	}
	public void setLateFee(Double lateFee) {
		this.lateFee = lateFee;
	}
	public Double getAdditionalInterest() {
		return additionalInterest;
	}
	public void setAdditionalInterest(Double additionalInterest) {
		this.additionalInterest = additionalInterest;
	}
	public String getLoanAccountNumber() {
		return loanAccountNumber;
	}
	public void setLoanAccountNumber(String loanAccountNumber) {
		this.loanAccountNumber = loanAccountNumber;
	}
	public Double getPrincipalBalance() {
		return principalBalance;
	}
	public void setPrincipalBalance(Double principalBalance) {
		this.principalBalance = principalBalance;
	}
	public Double getAmountDue() {
		return amountDue;
	}
	public void setAmountDue(Double amountDue) {
		this.amountDue = amountDue;
	}
	public Date getNextPaymentDate() {
		return nextPaymentDate;
	}
	public void setNextPaymentDate(Date nextPaymentDate) {
		this.nextPaymentDate = nextPaymentDate;
	}
	public void pay(double amount) {
		double balance = amount;
		if(lateFee>0) {
			balance = balance-lateFee;
			lateFee=0d;
		}
		if(additionalInterest>0) {
			balance = balance-additionalInterest;
			additionalInterest=0d;
		}
		principalBalance = principalBalance - balance;
		DateTime dt= new DateTime(nextPaymentDate);
		nextPaymentDate = dt.plusMonths(1).toDate();
		System.out.println("Next payment date is " + nextPaymentDate);
		
	}
	
	
}
