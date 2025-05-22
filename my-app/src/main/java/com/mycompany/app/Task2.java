package com.mycompany.app;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Task2 {
    public static void start() {
        System.setProperty("webdriver.chrome.driver", "D:\\chromedriver-win64\\chromedriver.exe");

        WebDriver driver = new ChromeDriver();
        try {
            driver.navigate().to("https://api.ipify.org/?format=json");

            WebElement preElement = driver.findElement(By.tagName("pre"));

            String responseText = preElement.getText();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(responseText);

            String ipAddress = (String) jsonObject.get("ip");

            System.out.println("Your IP address: " + ipAddress);

        } catch (Exception ex) {
            System.out.println("An error occurred");
            System.out.println(ex.getMessage());
        } finally {
            driver.quit();
        }
    }
}
