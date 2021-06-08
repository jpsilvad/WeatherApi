import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*; 
import java.lang.reflect.Type;
import java.util.ArrayList;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;


public class TestName {

    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        Gson gson = new Gson();

    	String searchCity = "https://www.metaweather.com/api/location/search/?query=";
		Scanner sc = new Scanner(System.in);  
		String woeidNmr;
	    	do {
	        	System.out.print("Enter a city: ");  
	        	String cityName = sc.nextLine();
	        	try {
	        		StringBuffer jsonStr = readJsonAsString(searchCity + cityName);
	                jsonStr.deleteCharAt(jsonStr.length()-1);
	                jsonStr.deleteCharAt(0);
	                
	                City jsonCityObj = gson.fromJson(jsonStr.toString(), City.class);

	                woeidNmr = jsonCityObj.woeid;
	                System.out.println(woeidNmr);
	        	}catch (Exception e){
	        		
	        		woeidNmr = "";
	        		System.out.println("City not found, please try again.");
	        	}
	        	
	    		
	    	}while(woeidNmr == null || woeidNmr == "");
	    	
	        
	        String searchWoeid = "https://www.metaweather.com/api/location/" + woeidNmr + "/2021/6/7/";
	    	    	
	        StringBuffer jsonWoeidStr = readJsonAsString(searchWoeid);
	                
	                
	        Type woeidListType = new TypeToken<ArrayList<Woeid>>(){}.getType();
	        
	        List<Woeid> woeidArray = gson.fromJson(jsonWoeidStr.toString(), woeidListType);
	        
	        Woeid woeid = woeidArray.get(0);
	        System.out.println("The temperature in Celsius is: " + woeid.the_temp);
	        System.out.println("The temperature in Fahrenheit is: " + ConvertToFar(woeid.the_temp));


    }

    public static StringBuffer readJsonAsString(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read); 

            
            return buffer;
        } finally {
            if (reader != null)
                reader.close();
        }

    }
    
    public static float ConvertToFar(String tempCelsius) {
    	float temp = Float.parseFloat(tempCelsius);
    	return ((temp*9)/5)+32;
    }


    static class City
    {
        String    woeid;
    }
    
    static class Woeid
    {
        String    the_temp;
        String 		created;
    }


}
