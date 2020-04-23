import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainApp implements Runnable {

    private Scanner scanner;

    private void startApp() {
        scanner = new Scanner(System.in);
        System.out.println("Wybierz po czym chcesz znaleźć miejsce dla którego wyświetlisz pogodę \n0 - Zakończ działanie \n1 - Nazwa Miasta \n2 - Kod pocztowy \n3 - Koordynaty \n4 - Nazwa miasta - pogoda na 5 dni");
        Integer name = scanner.nextInt();
        chooseTypeSearching(name);
    }

    private void chooseTypeSearching(Integer typeNumber) {
        switch (typeNumber) {
            case 0:
                break;
            case 1:
                connectByCityName();
                startApp();
                break;
            case 2:
                connectByZipCode();
                startApp();
                break;
            case 3:
                connectByCords();
                startApp();
                break;
            case 4:
                connectByCityForXDays();
                startApp();
                break;
        }
    }

    private void connectByCityName() {
        System.out.println("Podaj nazwę miasta: ");
        String cityName = scanner.next();
        String response = connectByCityName(cityName);
        parseJson(response);
    }

    public String connectByCityName(String cityName) {
        String response = null;
        try {
            response = new HttpService().connect(Config.APP_URL + "?q=" + cityName + "&appid=" + Config.APP_ID);
        } catch (IOException e) {
            e.printStackTrace();
            response = "404";
        }
        return response;
    }

    private void connectByZipCode() {
        System.out.println("Podaj kod pocztowy miasta: ");
        String zipcode = scanner.next();
        String response = connectByZipCode(zipcode);
        parseJson(response);
    }

    public String connectByZipCode(String zipcode) {
        String response = null;
        try {
            response = new HttpService().connect(Config.APP_URL + "?zip=" + zipcode + ",pl" + "&appid=" + Config.APP_ID);

        } catch (IOException e) {
            e.printStackTrace();
            response = "404";
        }
        return response;
    }

    private void connectByCords() {
        System.out.println("Podaj koordynaty x,y miasta: ");
        String cordX = scanner.next();
        String cordY = scanner.next();
        String response = connectByCords(cordX, cordY);
        parseJson(response);

    }

    public String connectByCords(String cordX, String cordY) {
        String response = null;

        try {
            response = new HttpService().connect(Config.APP_URL + "?lat=" + cordX + "&lon=" + cordY + "&appid=" + Config.APP_ID);

        } catch (IOException e) {
            e.printStackTrace();
            response = "404";
        }
        return response;

    }

    public void connectByCityForXDays() {
        System.out.println("Podaj nazwę miasta: ");
        String cityName = scanner.next();
        String response = connectByCityForXDays(cityName);
        parseJson2(response);

    }

    public String connectByCityForXDays(String cityName) {
        String response = null;
        try {
            response = new HttpService().connect(Config.APP_URL2 + "?q=" + cityName + "&appid=" + Config.APP_ID + "&units=metric");

        } catch (IOException e) {
            e.printStackTrace();
            response = "404";
        }
        return response;
    }

    private void parseJson2(String json) {


        JSONObject rootObject = new JSONObject(json);
        JSONArray xObject = rootObject.getJSONArray("list");
        List<Weather> weatherList = new ArrayList<>();

        if (rootObject.getInt("cod") == 200) {
            for (int i = 0; i < xObject.length(); i++) {
                JSONObject firstObject = xObject.getJSONObject(i).getJSONObject("main");
                    JSONObject mainObject = xObject.getJSONObject((i));
                if (mainObject.getString("dt_txt").contains("12:00:00")) {

                    Weather weather = new Weather();

                    weather.setData(mainObject.get("dt_txt").toString());

                    weather.setTemperature(Double.parseDouble(firstObject.get("temp").toString()));
                    weather.setPressure(Double.parseDouble(firstObject.get("pressure").toString()));

                    JSONObject cloudsObject = xObject.getJSONObject(i).getJSONObject("clouds");
                    weather.setClouds(Double.parseDouble(cloudsObject.get("all").toString()));
                    weather.setHumidity(Double.parseDouble(firstObject.get("humidity").toString()));

                    weatherList.add(weather);

                }
            }
        } else {
            System.out.println("Error");
        }
        System.out.println(weatherList);//pięknie działa :)ale  a jesntie teraz działa, pobierałeś zawsze pogodęe dla 0 obiektu zamist dla obiektu aktualnego czyli i:)

    }

    private void parseJson(String json) {
        double temperature;
        int clouds;
        int humidity;
        int pressure;


        JSONObject rootObject = new JSONObject(json);
        if (rootObject.getInt("cod") == 200) {
            JSONObject mainObject = rootObject.getJSONObject("main");
            DecimalFormat df = new DecimalFormat("#.##");
            temperature = mainObject.getDouble("temp");
            temperature = temperature - 273;

            humidity = mainObject.getInt("humidity");
            pressure = mainObject.getInt("pressure");
            JSONObject cloudsObject = rootObject.getJSONObject("clouds");
            clouds = cloudsObject.getInt("all");

            System.out.println("Temperatura: " + df.format(temperature) + " \u00b0C");
            System.out.println("Wilgotność: " + humidity + " %");
            System.out.println("Zachmurzenie: " + clouds + "%");
            System.out.println("Ciśnienie: " + pressure + " hPa");

        } else {
            System.out.println("Error");
        }
    }

    @Override
    public void run() {
        startApp();
    }
}
