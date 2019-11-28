import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class Waits {

    WebDriver driver;
    WebDriverWait wait;
    By cookieConsentBar = By.cssSelector("a[class*='dismiss-link']");
    By pilatesGroup = By.cssSelector("a[href*='pilates']");
    By product = By.cssSelector("li.post-61");
    By addToCartButton = By.cssSelector("button[name='add-to-cart']");
    By goToCartButton = By.cssSelector("a.cart-contents");

    By productRemove = By.cssSelector("a.remove");

    String correctCoupon = "10procent";
    String incorrectTest = "test";




    @BeforeEach
    public void driverSetup()
    {
        String fakeStore = "https://fakestore.testelka.pl/";

        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.navigate().to(fakeStore);

        wait = new WebDriverWait(driver,10);

        driver.findElement(cookieConsentBar).click();
        driver.findElement(pilatesGroup).click();
        driver.findElement(product).click();
        driver.findElement(addToCartButton).click();
        driver.findElement(goToCartButton).click();
    }

    @AfterEach
    public void driverClose()
    {
        driver.close();
        driver.quit();
    }

//    @DisplayName("Simple coupon test")
//    @ParameterizedTest(name = "Coupon: \"{0}\"")
//    @CsvSource({ " '', Proszę wpisać kod kuponu.",
//            "test, Kupon \"test\" nie istnieje!",
//            "10procent, Kupon został pomyślnie użyty."})
//    void simpleAddingCouponTest(String coupon, String expectedAlert){
//        applyCoupon(coupon);
//        Assertions.assertEquals(getAlertText(),expectedAlert, "Alert message was not what expected");
//    }



//    @Test
//    public void emptyCouponTest() {
//        applyCoupon("");
//        Assertions.assertEquals("Proszę wpisać kod kuponu.",getAlertText(), "Allert message was not what expected.");
//    }
//
//
//    @Test
//    public void correctCouponTest()
//    {
//        applyCoupon(correctCoupon);
//        Assertions.assertEquals("Kupon został pomyślnie użyty.", getAlertText(), "Allert message was not what expected.");
//    }
//
//
//    @Test
//    public void incorrectCouponTest()
//    {
//        applyCoupon(incorrectTest);
//        Assertions.assertEquals("Kupon " + incorrectTest + " nie istnieje!", getAlertText(), "Allert message was not what expected");
//    }

    @Test
    public void addingCouponWhenAlreadyAppliedTest()
    {
        applyCoupon(correctCoupon);
        waitForProcessingEnd();
        applyCoupon(correctCoupon);
        Assertions.assertEquals(getAlertText(),"Kupon został zastosowany!", "Alert message was not what expected");
    }

    @Test
    public void removingCouponTest() {
        applyCoupon(correctCoupon);
        waitForProcessingEnd();
        By removeLink = By.cssSelector("a.woocommerce-remove-coupon");
        wait.until(ExpectedConditions.elementToBeClickable(removeLink)).click();
        waitForProcessingEnd();
        Assertions.assertEquals(getAlertText(),"Kupon został usunięty", "Alert message was not what expected");
    }

    @Test
    public void removeProductInCart() {
        By deleteProduct = By.cssSelector("a.remove");
        WebElement titleProduct =  driver.findElement(By.cssSelector("td.product-name>a"));
        System.out.println(titleProduct.getText());
        wait.until(ExpectedConditions.elementToBeClickable(deleteProduct)).click();
        waitForProcessingEnd();
        Assertions.assertEquals(getAlertText(), "Usunięto:" + titleProduct.getText() +  ".");

    }

    public void deleteProductfromCart() {
        By deleteProduct = By.cssSelector("a.remove");
        driver.findElement(deleteProduct).click();
    }

    private void applyCoupon(String coupon){
        By couponField = By.cssSelector("input[name='coupon_code']");
        By couponButton = By.cssSelector("button[name='apply_coupon']");
        driver.findElement(couponField).sendKeys(coupon);
        driver.findElement(couponButton).click();
    }

    private  void waitForProcessingEnd(){
        By blockedUI = By.cssSelector("div.blockUI");
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(blockedUI,0));
        wait.until(ExpectedConditions.numberOfElementsToBe(blockedUI,0));
    }


    private String getAlertText() {
        By alert = By.cssSelector("[role='alert']");
        return  wait.until(ExpectedConditions.visibilityOfElementLocated(alert)).getText();
    }








}
