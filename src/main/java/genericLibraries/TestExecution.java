package genericLibraries;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.edge.EdgeOptions;

public class TestExecution {
	
	public PropertyFile pData= new PropertyFile();
	public WebDriver driver;
	
	/**
	 * It is used to open the url of the webpage.
	 */
	@BeforeClass
	
	public void openApp() {
		
		//	System.setProperty("WebDriver.chrome.driver","C:\\Users\\YuvarajV\\Downloads\\chromedriver-win64\\chromedriver.exe");
			WebDriverManager.chromedriver().setup();
	        ChromeOptions options = new ChromeOptions();
	        options.addArguments("--remote-allow-origins=*");
	        options.addArguments("--start-maximized");
	        options.addArguments("--disable-extensions");
	        driver = new ChromeDriver(options);
	        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
	     
	     /*
		System.setProperty("WebDriver.edge.driver","C:\\Users\\YuvarajV\\Downloads\\edgedriver_win64.exe");
		EdgeOptions options = new EdgeOptions();
		options.setCapability("ms:edgeOptions", "{\"args\": [\"--disable-features=BlockThirdPartyCookies\"]}");
		driver= new EdgeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		 */  
	     
	}
	
	/**
	 * This method is used to login the application.
	 * @throws InterruptedException 
	 * @throws IOException 
	 */

	@BeforeMethod
	public void Login() throws InterruptedException, IOException {
		
		driver.get(pData.getData("url"));
		driver.findElement(By.name("loginfmt")).sendKeys(pData.getData("userName"));
		driver.findElement(By.id("idSIButton9")).click();
		driver.findElement(By.name("passwd")).sendKeys(pData.getData("password"));
		driver.findElement(By.xpath("//input[@value='Sign in']")).click();
		driver.findElement(By.id("KmsiCheckboxField")).click();
		driver.findElement(By.xpath("//input[@value='Yes']")).click();
		driver.findElement(By.xpath("//span[contains(text(),'Dive in!')]")).click();
		WebElement element1 = driver.findElement(By.xpath("//span[@id='14_label']"));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", element1);
		Thread.sleep(3000);
			
	}
	/**
	 * This Method is used to read the xml node value and generating the WebElement locator.
	 * The generated xpath is mapping the webElements.
	 * @throws InterruptedException
	 */
	
	@Test
	
	public void execution() throws InterruptedException {
		
		try {
			File inputFile = new File(pData.getData("path"));
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			NodeList nodeList = doc.getElementsByTagName("Node");

				for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					String elementType = element.getAttribute("i:type");

					if (elementType.equals("CommandUserAction")) {
						String controlLabel = element.getElementsByTagName("ControlLabel").item(0).getTextContent();

						if (controlLabel != null && !controlLabel.isEmpty()) {
							String commandName = element.getElementsByTagName("CommandName").item(0).getTextContent();
							System.out.println("Control Label: " + controlLabel);
							System.out.println("Command Name: " + commandName);
							System.out.println("--------------------------------------");
							String generatedXPath1 = "//span[text()='" + controlLabel + "']";
							String generatedXPath2 = "//li[text()='" + controlLabel + "']";

							while (true) {
								try {
									// Find and interact with the element based on the generatedXPath1
									WebElement elementToInteract = driver.findElement(By.xpath(generatedXPath1));
									JavascriptExecutor js1 = (JavascriptExecutor) driver;
									js1.executeScript("arguments[0].click();", elementToInteract);
									Thread.sleep(3000);

									if (!conditionMet()) {
										break; 
									}
								} catch (NoSuchElementException ex) {
									// generatedXPath1 didn't find the element, so try with generatedXPath2
									try {
										WebElement elementToInteract = driver.findElement(By.xpath(generatedXPath2));
										JavascriptExecutor js2 = (JavascriptExecutor) driver;
										js2.executeScript("arguments[0].click();", elementToInteract);
										Thread.sleep(3000);
									
										if (!conditionMet()) {
											break; 
										}
									} catch (NoSuchElementException ex2) {

										break; 
									}
								}
							}
						}

					} else if (elementType.equals("MenuItemUserAction")) {

						NodeList stringElements = element.getElementsByTagName("d7p1:string");

						for (int j = 0; j < stringElements.getLength(); j++) {
							Node stringNode = stringElements.item(j);
							String MenuItem = stringNode.getTextContent();
							String formattedMenuItem = formatCamelCase(MenuItem);
							System.out.println(formattedMenuItem);

							if (formattedMenuItem.equals("Mainmenu")) {
								driver.findElement(By.xpath("//span[@data-dyn-title='Modules']")).click();

							}
							else {
								String xpath = "//a[text()='" + formattedMenuItem + "']";
								WebElement elementToInteract = driver.findElement(By.xpath(xpath));
								JavascriptExecutor js3 = (JavascriptExecutor) driver;
								js3.executeScript("arguments[0].click();", elementToInteract);
								Thread.sleep(2000);
							}
						}
						Thread.sleep(3000);
						driver.findElement(By.xpath("//button[text()='Expand all ']")).click();
						
						
						String Label = element.getElementsByTagName("Description").item(0).getTextContent();
						
						 if (Label.equals("Products")) {
								driver.findElement(By.xpath("(//a[text()='Products'])[2]")).click();
							}
						 
						 else {
						
						 String[] parts = Label.split(">");  // Split the description by '>'
	                        if (parts.length > 2) {
	                            String targetText = parts[parts.length - 1].trim();  // Get the last part and trim any spaces
	                            targetText = targetText.replace(".", "");
	                            System.out.println(targetText);
	                            String xpath = "//a[text()='" + targetText + "']";
								WebElement elementToInteract = driver.findElement(By.xpath(xpath));
								JavascriptExecutor js3 = (JavascriptExecutor) driver;
								js3.executeScript("arguments[0].click();", elementToInteract);
								Thread.sleep(1000);
	                        }
						  }
					}

					else if (elementType.equals("PropertyUserAction")) {
						
	                    if (element.getElementsByTagName("ControlName").getLength() > 0) {
	                        String controlName = element.getElementsByTagName("ControlName").item(0).getTextContent();
	                        String generatedXPath1 = "//input[@name='" + controlName + "']";
							WebElement elementToInteract = driver.findElement(By.xpath(generatedXPath1));
	                        System.out.println("ControlName: " + controlName);
	                        
	                        if (element.getElementsByTagName("Value").getLength() > 0) {
		                        String value = element.getElementsByTagName("Value").item(0).getTextContent();
		                        elementToInteract.sendKeys(value);
		                        System.out.println("Value: " + value);
		                    }	
	                    }
	                    
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static boolean conditionMet() {

		return false;
	}

	private static String formatCamelCase(String input) {
		StringBuilder output = new StringBuilder();
		for (int i = 0; i < input.length(); i++) {
			char currentChar = input.charAt(i);

			if (i == 0) {
				output.append(Character.toUpperCase(currentChar));
			} else {
				if (Character.isUpperCase(currentChar)) {
					output.append(" ").append(Character.toLowerCase(currentChar));
				} else {
					output.append(currentChar);
				}
			}
		}
		return output.toString();
	}


	
	
/*
 * This method is used to close the application.	
 */
	
/*
	@AfterClass
	public void closeApp() {
		driver.quit();
	}
	
*/
}
