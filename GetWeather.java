/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package getweather;

import java.io.BufferedReader;
import java.time.*;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;
import java.util.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author Julia
 */
public class GetWeather {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        Boolean runApi = true;
        do {
            Gson gson = new Gson();

            String searchCity = "https://www.metaweather.com/api/location/search/?query=";
            
            String cityName;

            String woeidNmr;
            do {
                System.out.println("Enter '0' to quit.");
                System.out.print("Enter a city: ");
                Scanner sc = new Scanner(System.in);
                cityName = sc.nextLine();
                
                cityName = URLEncoder.encode(cityName, StandardCharsets.UTF_8.toString());
                
               //cityName = cityName.trim().replace(" ", "%20");
                
                if(cityName.equals("0"))
                {
                    return;
                }
                StringBuffer jsonStr = readJsonAsString(searchCity + cityName);

                if (!jsonStr.toString().trim().equals("[]")) {
                    Type CityListType = new TypeToken<ArrayList<City>>() {
                    }.getType();
                    List<City> CityArray = gson.fromJson(jsonStr.toString(), CityListType);
                    woeidNmr = CityArray.get(0).woeid;
                    cityName = CityArray.get(0).title;
                } else {
                    woeidNmr = "";
                    System.out.println("City not found, please try again.");
                }

            } while (woeidNmr == null || woeidNmr.equals(""));

            LocalDate todayDt = LocalDate.now();
            int day = todayDt.getDayOfMonth();
            int month = todayDt.getMonthValue();
            int year = todayDt.getYear();

            String searchWoeid = "https://www.metaweather.com/api/location/" + woeidNmr + "/" + year + "/" + month + "/" + day;

            StringBuffer jsonWoeidStr = readJsonAsString(searchWoeid);
            Type woeidListType = new TypeToken<ArrayList<Woeid>>() {
            }.getType();
            List<Woeid> woeidArray = gson.fromJson(jsonWoeidStr.toString(), woeidListType);

            Woeid woeid = woeidArray.get(0);
            System.out.println(cityName + ": Temperature in Celsius is: " + woeid.the_temp);
            System.out.println(cityName + ": Temperature in Fahrenheit is: " + ConvertToFar(woeid.the_temp));
        } while (true);
    }

    public static StringBuffer readJsonAsString(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1) {
                buffer.append(chars, 0, read);
            }

            return buffer;
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    public static float ConvertToFar(String tempCelsius) {
        float temp = Float.parseFloat(tempCelsius);
        return ((temp * 9) / 5) + 32;
    }

    static class City {

        String woeid;
        String title;
    }

    static class Woeid {

        String the_temp;
        String created;
    }
}
