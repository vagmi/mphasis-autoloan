package in.vagmim.autoloan.selenium;

import java.io.File;

import org.junit.Assert;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.skife.jdbi.v2.*;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
public class H2Test {
	
	public static interface H2Account {
		@SqlUpdate("insert into accounts(name, amount) values (:name,:amount)")
		public void insertAccount(@Bind("name") String acctName, @Bind("amount") double amount);
		
		@SqlQuery("select id from accounts where name=:name")
		public Integer accountIdFor(@Bind("name") String name);
		
		@SqlQuery("select sum(loans.amount) from accounts join loans "
				+ "on loans.account_id=accounts.id where accounts.name=:name")
		public Double getTotalLoanValue(@Bind("name") String name);
		
		@SqlQuery("select count(loans.amount) from accounts join loans "
				+ "on loans.account_id=accounts.id where accounts.name=:name")
		public Integer getTotalLoanCount(@Bind("name") String name);
	}
	public static interface H2Loan {
		@SqlUpdate("insert into loans(loan_name, amount,account_id) values (:loan_name,:amount,:account_id)")
		public void insertLoan(@Bind("loan_name") String loanName, 
							   @Bind("amount") double amount,
							   @Bind("account_id") int accountId);
		
		
	}
	
	public static String connString = "jdbc:h2:file:~/work/mphasis/autoloan/target/db";
	Handle dbi;
	@Before
	public void setupDatabases() {
		FileUtils.deleteQuietly(new File("target/db.h2.db"));
		dbi = DBI.open(connString);
		dbi.execute("create table accounts (id integer auto_increment primary key,"+
					"name varchar(255),"
					+ "amount numeric(10,2))");
		dbi.execute("create table loans (id integer auto_increment primary key,"+
				"loan_name varchar(255),"
				+ "amount numeric(10,2),"
				+ "account_id integer)");
		
		H2Account account = dbi.attach(H2Account.class);
		H2Loan loan = dbi.attach(H2Loan.class);
		account.insertAccount("Account 1", 2500d);
		account.insertAccount("Account 2", 3500d);
		int accountId1 = account.accountIdFor("Account 1");
		int accountId2 = account.accountIdFor("Account 2");
		for(int i=0;i<10;i++) {
			loan.insertLoan("Loan " + i, 25000d, accountId1);
			loan.insertLoan("Loan " + i, 25000d, accountId2);
		}
	}
	@Test
	public void getLoanStats(){
		H2Account account = dbi.attach(H2Account.class);
		Assert.assertEquals(new Double(250000d), account.getTotalLoanValue("Account 1"));
		Assert.assertEquals(new Integer(10), account.getTotalLoanCount("Account 1"));

	}
	@After
	public void close(){
		dbi.close();
	}
}
