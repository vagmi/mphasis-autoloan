package in.vagmim.autoloan.models;

import java.util.ArrayList;
import java.util.List;

public class Customer {
	List<Account> accounts;
	List<Loan> loans;
	public Customer(){
		this.accounts = new ArrayList<Account>();
		this.loans = new ArrayList<Loan>();
	}
	public List<Account> getAccounts() {
		return accounts;
	}
	public List<Loan> getLoans() {
		return loans;
	}
	public void payForLoan(double amount, String loanNumber) {
		Loan loan = getLoanFor(loanNumber);
		Account account = getDefaultAccount();
		loan.pay(amount);
		account.deduct(amount);
	}
	public Loan getLoanFor(String loanNumber) {
		for(Loan l : this.loans) {
			if(l.getLoanAccountNumber().equals(loanNumber)) 
				return l;
		}
		return null;
	}
	public Account getDefaultAccount() {
		return this.accounts.get(0);
	}
}
