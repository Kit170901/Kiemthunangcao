package webtest;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.poi.hpsf.Date;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class DangNhapPageTest {
	public WebDriver driver;
	String user_name = "lucdtps17380@fpt.edu.vn";
	String pass_word = "UT<uG4n;VR.<QG3";
	Map<String, Object[]> NgResult;

	public String url = "https://www.w3schools.com/";; // địa chỉ trang web cần kiểm tra
	public String diverPath = "C:\\Users\\Admin\\Downloads\\Compressed\\chromedriver.exe"; // đường dẫn đến driver của  google chrome trên máy

	HSSFWorkbook workbook;
	HSSFSheet sheet;

	@BeforeClass(alwaysRun = true)
	public void suiteSetup() {
		workbook = new HSSFWorkbook();
		sheet = workbook.createSheet("TestNG Result Summary");
		NgResult = new LinkedHashMap<String, Object[]>();
		NgResult.put("1", new Object[] { "Test Step No.", "Action", "Expected Output", "Actual Output" });

		try {
			System.setProperty("webdriver.chrome.driver", diverPath);// setProperty cho phép thiết lập driver để remote
																		// trình duyệt google chrome được sử dụng
			driver = (WebDriver) new ChromeDriver();// Khởi tạo đối tượng driver
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			throw new IllegalStateException("Can't start webdriver");
		}
	}

	@Test(priority = 1)
	public void launchWebsite() {
		try {
			driver.get(url);
			driver.manage().window().maximize();
			driver.findElement(By.id("w3loginbtn"))
					.click();
			NgResult.put("2", new Object[] { "1.", "Truy cập website", "Trang web đã được truy cập", "Pass" });

		} catch (Exception e) {
			// TODO: handle exception
			NgResult.put("2", new Object[] { "1.", "Truy cập website", "Trang web đã được truy cập", "Fail" });
			Assert.assertTrue(false);
		}
	}

	@Test(priority = 2)
	public void FillLogin() throws Exception {
		NgResult.put("3", new Object[] { "2.", "Nhập dữ liệu để đăng nhập", "Biểu mẫu đăng nhập được điền", "Fail" });

		WebElement username = driver.findElement(By.name("email"));
		username.sendKeys(user_name);

		WebElement password = driver.findElement(By.name("current-password"));
		password.sendKeys(pass_word);
		Thread.sleep(1000);
		String us = username.getAttribute("value");
		String ps = password.getAttribute("value");

		assertTrue(us.contentEquals(user_name) && ps.contentEquals(pass_word));
		NgResult.put("3", new Object[] { "2.", "Nhập dữ liệu để đăng nhập", "Biểu mẫu đăng nhập được điền", "Pass" });
	}

	@Test(priority = 3)
	public void DoLogin() throws Exception {
		NgResult.put("5",
				new Object[] { "4.", "Bấm đăng nhập và điều hướng tới trang chủ", "Đăng nhập thành công", "Fail" });
		Thread.sleep(1000);
		
		driver.findElement(By.name("current-password")).sendKeys(Keys.ENTER);
		Thread.sleep(10000);
		System.out.println(driver.getTitle());
		assertTrue(driver.getTitle().equals("My learning | W3Schools"));
		NgResult.put("5",
				new Object[] { "4.", "Bấm đăng nhập và điều hướng tới trang chủ", "Đăng nhập thành công", "Pass" });
	}

	@AfterClass
	public void suiteTearDown() {
		// Lấy bộ key của testcaseResult
		Set<String> keyset = NgResult.keySet();
		int rownumber = 0;
		for (String key : keyset) {
			Row row = sheet.createRow(rownumber++);// Tạo hàng
			Object[] objArr = NgResult.get(key);
			int cellnumber = 0;
			// Tạo cột và ghi dữ liệu từ testcaseResult
			for (Object obj : objArr) {
				Cell cell = row.createCell(cellnumber++);
				if (obj instanceof Date)
					cell.setCellValue((java.util.Date) obj);
				if (obj instanceof Boolean)
					cell.setCellValue((Boolean) obj);
				if (obj instanceof String)
					cell.setCellValue((String) obj);
				if (obj instanceof Double)
					cell.setCellValue((Double) obj);
			}
		}
		driver.close();
		try {
			FileOutputStream fos = new FileOutputStream(new File("TestCaseAssignment.xls"));// Tạo luồng mới
			workbook.write(fos);// Ghi file
			fos.close();
			System.out.println("Successfully");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}