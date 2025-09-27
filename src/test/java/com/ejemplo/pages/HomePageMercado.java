package com.ejemplo.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePageMercado {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By mexicoLink = By.xpath(
        "//a[contains(@href,'mercadolibre.com.mx') or .//span[normalize-space(.)='México']]"
    );
    private final By searchBox = By.cssSelector("input#cb1-edit, input[name='as_word']");

    public HomePageMercado(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public HomePageMercado open() {
        driver.get("https://www.mercadolibre.com/");
        return this;
    }

    public HomePageMercado selectMexico() {
        WebElement link = wait.until(ExpectedConditions.elementToBeClickable(mexicoLink));
        link.click();
        return this;
    }

    public SearchResultsMercado search(String query) {
        WebElement box = wait.until(ExpectedConditions.elementToBeClickable(searchBox));
        box.clear();
        box.sendKeys(query);
        box.sendKeys(Keys.ENTER);
        return new SearchResultsMercado(driver);
    }
}
