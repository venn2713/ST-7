package com.mycompany.app;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileWriter;
import java.io.PrintWriter;

public class Task3 {
    public static void start() {
        System.setProperty("webdriver.chrome.driver", "D:\\chromedriver-win64\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        try {
            String apiUrl = "https://api.open-meteo.com/v1/forecast?latitude=56&longitude=44&hourly=temperature_2m,rain&current=cloud_cover&timezone=Europe%2FMoscow&forecast_days=1&wind_speed_unit=ms";
            driver.get(apiUrl);

            WebElement preTag = driver.findElement(By.tagName("pre"));
            String jsonResponse = preTag.getText();

            JSONParser jsonParser = new JSONParser();
            JSONObject rootObj = (JSONObject) jsonParser.parse(jsonResponse);

            JSONObject hourlyObj = (JSONObject) rootObj.get("hourly");
            JSONArray timeArr = (JSONArray) hourlyObj.get("time");
            JSONArray tempArr = (JSONArray) hourlyObj.get("temperature_2m");
            JSONArray rainArr = (JSONArray) hourlyObj.get("rain");

            try (PrintWriter out = new PrintWriter(new FileWriter("../result/forecast.txt"))) {
                String format = "| %-3s | %-19s | %-11s | %-10s |%n";
                String separator = "+-----+---------------------+-------------+------------+%n";

                out.printf(separator);
                out.printf(format, "№", "Дата/время", "Температура", "Осадки (мм)");
                out.printf(separator);

                for (int idx = 0; idx < timeArr.size(); idx++) {
                    String dateTime = (String) timeArr.get(idx);
                    double temperature = ((Number) tempArr.get(idx)).doubleValue();
                    double precipitation = ((Number) rainArr.get(idx)).doubleValue();
                    out.printf(format, idx + 1, dateTime, String.format("%.1f", temperature), String.format("%.2f", precipitation));
                }
                out.printf(separator);
            }

            System.out.println("Прогноз успешно сохранён в result/forecast.txt");

        } catch (Exception ex) {
            System.out.println("Ошибка при обработке прогноза погоды");
            System.out.println(ex.getMessage());
        } finally {
            driver.quit();
        }
    }
}
