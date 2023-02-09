import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import java.time.Duration;
import java.util.ArrayList;

public class FirstTest {

    /**
     * 2. navigate().to - переход на страницу
     *    driver.navigate().to("https://next.privat24.ua/money-transfer/card");
     *  3. back - переход назад
     *    driver.navigate().back();
     *  4. forward - переход вперед
     *     driver.navigate().forward();
     *  5. refresh - обновление странциы
     *     driver.navigate().refresh();
     *  7. остановить driver и все окна в браузере.
     *     driver.quit();
     */

    // TEST DATA
    static String BASE_URL = "https://next.privat24.ua/money-transfer/card";
    String cardFromExample = "4004159115449011";
    String cardForCheck = "4004 1591 1544 9011";

    static WebDriver driver = new ChromeDriver();

    // UI ELEMENTS
    By cardNumberFrom = By.xpath(".//input[@data-qa-node='numberdebitSource']");
    By expDate = By.xpath(".//input[@data-qa-node='expiredebitSource']");
    By cvv = By.xpath(".//input[@data-qa-node='cvvdebitSource']");
    By nameFrom = By.xpath(".//input[@data-qa-node='firstNamedebitSource']");
    By surnameFrom = By.xpath(".//input[@data-qa-node='lastNamedebitSource']");
    By cardTo = By.xpath(".//input[@data-qa-node='numberreceiver']");
    By nameTo = By.xpath(".//input[@data-qa-node='firstNamereceiver']");
    By surnameTo = By.xpath(".//input[@data-qa-node='lastNamereceiver']");
    By amount = By.xpath(".//input[@data-qa-node='amount']");
    By toggleComment = By.xpath(".//span[@data-qa-node='toggle-comment']");
    By comment = By.xpath(".//textarea[@data-qa-node='comment']");
    By btnAddToBasket = By.xpath(".//button[@type='submit']");
    By termsLink = By.xpath(".//a[@href='https://privatbank.ua/terms']");

    @BeforeAll
    static void defineChromeDriver(){
        driver.get("https://next.privat24.ua/");
    }

    @AfterAll
    static void closeChromeDriver(){
        driver.close();
    }

    @Test
    void checkAddToBasketMinPaymentSum() {
        driver.navigate().to(BASE_URL);
        //pre-condition: expecting loading web elements
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        //driver.getCurrentUrl();
        driver.findElement(cardNumberFrom).sendKeys(cardFromExample);
        driver.findElement(expDate).sendKeys("0725");
        driver.findElement(cvv).sendKeys("123");
        driver.findElement(nameFrom).sendKeys("Vaselina");
        driver.findElement(surnameFrom).sendKeys("Petrenko");
        driver.findElement(cardTo).sendKeys("5309233034765085");
        driver.findElement(nameTo).sendKeys("Irina");
        driver.findElement(surnameTo).sendKeys("Ivanenko");
        driver.findElement(amount).sendKeys("500");
        driver.findElement(toggleComment).click();
        driver.findElement(comment).sendKeys("TEST_EXAMPLE");
        driver.findElement(btnAddToBasket).submit(); // только если это tag form.

        Assertions.assertEquals(cardForCheck, driver.findElement(By.xpath(".//span[@data-qa-node='payer-card']")).getText());
    }

    @Test
    void checkSwitchToNewWindow() {
        driver.navigate().to(BASE_URL);
        driver.findElement(termsLink).click();
        ArrayList<String> tabs = new ArrayList<> (driver.getWindowHandles());
        driver.switchTo().window(tabs.get(tabs.size()-1));
        Assertions.assertEquals("https://privatbank.ua/terms", driver.getCurrentUrl());
        Assertions.assertEquals("Умови та правила", driver.getTitle());
        driver.close();
        driver.switchTo().window(tabs.get(0));
    }
}
