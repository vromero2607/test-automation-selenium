package com.ejemplo.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.NumberFormat;
import java.text.ParseException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchResultsMercado {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // --- Selectores ---
    private final By resultsContainer = By.cssSelector("ol.ui-search-layout, ol.ui-search-layout--stack");
    private final By itemCard = By.cssSelector("li.ui-search-layout__item, li.ui-search-layout__item > div");
    private final By itemTitle = By.cssSelector("h2.ui-search-item__title, h2.ui-search-item__title.shops__item-title");
    private final By itemPrice = By.cssSelector("span.andes-money-amount__fraction");

    private final By filterNew = By.xpath("//a[contains(.,'Nuevo') or contains(.,'Nuevos')]");
    private final By locationSectionToggle = By.xpath("//span[contains(text(),'Ubicación') or contains(text(),'Location')]");
    private final By locationCdmx = By.xpath("//a[contains(.,'CDMX') or contains(.,'Ciudad de México')]");
    private final By sortDropdown = By.cssSelector("button.andes-dropdown__trigger, div.ui-search-sort-filter");
    private final By sortPriceDesc = By.xpath("//a[contains(.,'Mayor precio') or contains(.,'mayor a menor precio')]");

    public SearchResultsMercado(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // Espera a que los resultados se refresquen después de un click
    private void waitUntilResultsRefresh() {
        WebElement old = null;
        try { old = driver.findElement(resultsContainer); } catch (NoSuchElementException ignored) {}
        if (old != null) {
            try {
                new WebDriverWait(driver, Duration.ofSeconds(15))
                        .until(ExpectedConditions.stalenessOf(old));
            } catch (Exception ignored) {}
        }
        wait.until(ExpectedConditions.presenceOfElementLocated(resultsContainer));
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(itemCard, 0));
    }

    private void scrollIntoView(WebElement el) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", el);
    }

    // --- Acciones ---
    public SearchResultsMercado filterNew() {
        try {
            WebElement link = wait.until(ExpectedConditions.elementToBeClickable(filterNew));
            scrollIntoView(link);
            link.click();
            waitUntilResultsRefresh();
        } catch (TimeoutException ignored) {}
        return this;
    }

    public SearchResultsMercado filterCDMX() {
        try {
            List<WebElement> toggles = driver.findElements(locationSectionToggle);
            if (!toggles.isEmpty()) {
                try { toggles.get(0).click(); } catch (Exception ignored) {}
            }
            WebElement link = wait.until(ExpectedConditions.elementToBeClickable(locationCdmx));
            scrollIntoView(link);
            link.click();
            waitUntilResultsRefresh();
        } catch (TimeoutException ignored) {}
        return this;
    }

    public SearchResultsMercado sortByPriceDesc() {
        try {
            WebElement dd = wait.until(ExpectedConditions.elementToBeClickable(sortDropdown));
            dd.click();
            WebElement desc = wait.until(ExpectedConditions.elementToBeClickable(sortPriceDesc));
            desc.click();
            waitUntilResultsRefresh();
        } catch (TimeoutException ignored) {}
        return this;
    }

    // --- Lectura de productos ---
    public List<Product> topN(int n) {
        List<Product> out = new ArrayList<>();
        NumberFormat nf = NumberFormat.getInstance(new Locale("es", "MX"));

        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(itemCard, 0));
        int total = Math.min(n, driver.findElements(itemCard).size());

        for (int i = 0; i < total; i++) {
            int attempts = 0;
            while (attempts < 3) {
                try {
                    List<WebElement> fresh = driver.findElements(itemCard);
                    WebElement card = fresh.get(i);

                    String name = safeText(card, itemTitle);
                    String priceTxt = safeText(card, itemPrice);
                    long price = parsePrice(priceTxt, nf);

                    out.add(new Product(name, price));
                    break; // ok, paso al siguiente
                } catch (StaleElementReferenceException e) {
                    attempts++;
                    try { Thread.sleep(200); } catch (InterruptedException ignored) {}
                }
            }
        }
        return out;
    }

    public void printTopNToConsole(int n) {
        List<Product> products = topN(n);
        System.out.println("==== Top " + products.size() + " productos ====");
        for (Product p : products) {
            System.out.println(p.name + " - $" + p.price);
        }
    }

    // --- Helpers ---
    private String safeText(WebElement parent, By locator) {
        try {
            return parent.findElement(locator).getText().trim();
        } catch (Exception e) {
            return "";
        }
    }

    private long parsePrice(String txt, NumberFormat nf) {
        try {
            return nf.parse(txt.replaceAll("[^0-9]", "")).longValue();
        } catch (ParseException e) {
            return 0;
        }
    }

    // --- POJO Producto ---
    public static class Product {
        public final String name;
        public final long price;
        public Product(String name, long price) {
            this.name = name;
            this.price = price;
        }
    }
}
