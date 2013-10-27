package in.vagmim.autoloan.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import cucumber.api.java.Before;

public class SharedDriver extends EventFiringWebDriver {
	public static ChromeDriver CHROME_DRIVER = new ChromeDriver();
	
	public SharedDriver() {
		super(CHROME_DRIVER);
	}
	@Before
	public void clearCookies(){
		manage().deleteAllCookies();
	}
	@Override
	public void close(){
		// do nothing
	}
	static {
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run(){
				CHROME_DRIVER.close();
			}
		});
	}

}
