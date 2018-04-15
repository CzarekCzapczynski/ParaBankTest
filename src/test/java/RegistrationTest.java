import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RegistrationTest {
    WebDriver driver = new ChromeDriver();

    public void isElementDisplayed(String elementParam, boolean isCSS) {
        WebElement element;
        if (isCSS) {
            element = driver.findElement(By.cssSelector(elementParam));
        } else {
            element = driver.findElement(By.xpath(elementParam));
        }
        do {
            System.out.println("waiting...");
        } while(!element.isDisplayed());
        System.out.println(element + " founded");
    }

    public void register (String firstName, String lastName,
                          String street, String city, String state, String zipCode,
                          String ssn,
                          String username, String password, String repeatedPassword) {

        driver.findElement(By.cssSelector("[id='customer.firstName']")).sendKeys(firstName);
        driver.findElement(By.cssSelector("[id='customer.lastName']")).sendKeys(lastName);
        driver.findElement(By.cssSelector("[id$='street']")).sendKeys(street);
        driver.findElement(By.cssSelector("[id$='city']")).sendKeys(city);
        driver.findElement(By.cssSelector("[id$='state']")).sendKeys(state);
        driver.findElement(By.cssSelector("[id$='zipCode']")).sendKeys(zipCode);
        driver.findElement(By.cssSelector("[id$='ssn']")).sendKeys(ssn);
        driver.findElement(By.cssSelector("[id$='username']")).sendKeys(username);
        driver.findElement(By.cssSelector("[id$='password']")).sendKeys(password);
        driver.findElement(By.cssSelector("#repeatedPassword")).sendKeys(repeatedPassword);
        driver.findElement(By.cssSelector("[value='Register']")).click();
    }

    @Test(priority = 0)
    public void openRegisterPage() {
        String register = "[href^='register.htm']";

        driver.get("http://parabank.parasoft.com");
        isElementDisplayed(register, true);
        driver.findElement(By.cssSelector(register)).sendKeys(Keys.RETURN);
    }

    @Test(priority = 1)
    public void shouldRegister() {
        String password = "pass1234";
        String confirm = "//p[contains(.,'created successfully')]";

        openRegisterPage();
        register("Ogórek", "Szklarniowy",
                "Ogórkowa", "Grządki", "Pole", "0000",
                "11", "ogor115", password ,password);
        isElementDisplayed(confirm, false);
        Assert.assertTrue(driver.findElement(By.xpath(confirm)).isDisplayed());
    }

    @Test(priority = 2)
    public void shouldNotRegisterUserExist() {
        String password = "pass1234";
        String error = "[id='customer.username.errors']";

        for (int i=1; i<=2; i++) {
            openRegisterPage();
            register("Ogórek", "Szklarniowy",
                    "Ogórkowa", "Grządki", "Pole", "0000",
                    "11", "ogor2", password ,password);
        }
        Assert.assertTrue(driver.findElement(By.cssSelector(error)).isDisplayed());
    }

    @Test(priority = 3)
    public void shouldNotRegisterDifferentPassword() {
        String password = "pass1234";
        String error = "[id='repeatedPassword.errors']";

        openRegisterPage();
        register("Ogórek", "Szklarniowy",
                "Ogórkowa", "Grządki", "Pole", "0000",
                "11", "ogor3", password ,password + "a");
        Assert.assertTrue(driver.findElement(By.cssSelector(error)).isDisplayed());
    }

    @Test(priority = 4)
    public void shouldNotRegisterEmptyUsername() {
        String password = "pass1234";
        String error = "[id='customer.username.errors']";

        openRegisterPage();
        register("Ogórek", "Szklarniowy",
                "Ogórkowa", "Grządki", "Pole", "0000",
                "11", "", password ,password);
        Assert.assertTrue(driver.findElement(By.cssSelector(error)).isDisplayed());
    }
}
