package in.vagmim.autoloan;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class RunExcelTest {
	public static class UserAccount {
		public String accountName;
		public String name;
		public Double amount;
	}
	@Test
	public void excelFileShouldHaveTheRightValues() throws IOException{
		InputStream excelFile = this.getClass().getResourceAsStream("/accounts.xls");
		HSSFWorkbook workbook = new HSSFWorkbook(excelFile);
		HSSFSheet sheet = workbook.getSheet("Accounts");
		Iterator<Row> rowIterator = sheet.rowIterator();
		List<UserAccount> accounts = new ArrayList<UserAccount>();
		while(rowIterator.hasNext()){
			Row row = rowIterator.next();
			if(row.getRowNum()==0) continue;
			UserAccount uacc = new UserAccount();
			uacc.accountName = row.getCell(0).getStringCellValue();
			uacc.name = row.getCell(1).getStringCellValue();
			uacc.amount = row.getCell(2).getNumericCellValue();
			accounts.add(uacc);
		}
		Integer startingAccountNumber = 12345;
		Double startingAmount = 50000d;
		for(int i=0;i<10;i++){
			assertEquals(startingAccountNumber.toString(),accounts.get(i).accountName);
			assertEquals("Some random name " + (i+1),accounts.get(i).name);
			assertEquals(startingAmount,accounts.get(i).amount);
			startingAccountNumber = startingAccountNumber + 1;
			startingAmount = startingAmount + 1;
		}
	}
}
