package com.ejemplo.core;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverFactory {

    public static WebDriver create() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions opts = new ChromeOptions();
    
        opts.addArguments("--start-maximized");
        return new ChromeDriver(opts);
    }
}
