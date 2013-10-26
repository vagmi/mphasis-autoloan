package in.vagmim.autoloan.models;

public class Account {

	String accountNumber;
	Double availableBalance;
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public Double getAvailableBalance() {
		return availableBalance;
	}
	public void setAvailableBalance(Double availableBalance) {
		this.availableBalance = availableBalance;
	}
	public void deduct(double amount) {
		availableBalance = availableBalance - amount;
	}
	
}
