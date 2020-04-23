import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;
import org.junit.jupiter.api.*;
import java.io.IOException;
public class MainAppTest {
    @BeforeAll
    static void init() {
        System.out.println("Rozpoczęcie testów aplikacji");
    }
    @Test
    @DisplayName("Główny test połączenia")
    @Tag("dev")
    void connectionTest() {
        HttpService httpService = new HttpService();
        JSONObject rootObject = null;
        try {
            String respone = httpService.connect(Config.APP_URL + "?q=" + "Warszawa" + "&appid=" + Config.APP_ID);
            rootObject = new JSONObject(respone);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assertions.assertTrue(rootObject.getInt("cod") == 200);
    }
    @Test
    @DisplayName("Test pogody dla miasta Warszawa")
    void connectByCityNameTest() {
        MainApp mainApp = new MainApp();
        String responseTest = mainApp.connectByCityName("Warsaw");
        JSONObject jsonObject = new JSONObject(responseTest);
        Assertions.assertEquals(200, jsonObject.getInt("cod"));
        Assertions.assertEquals("Warsaw", jsonObject.getString("name"));
    }

    @Test
    @DisplayName("Test pogody dla kodu pocztowego 09-140")
    void connectByZipCodeTest(){
        MainApp mainApp = new MainApp();
        String responseTest = mainApp.connectByZipCode("09-140");
        JSONObject jsonObject = new JSONObject(responseTest);
        Assertions.assertEquals(200, jsonObject.getInt("cod"));
        Assertions.assertEquals("Raciąż", jsonObject.getString("name"));
    }

    @Test
    @DisplayName("Test pogody dla koordynatów x=35, y=139")
    void connectByCords(){
        MainApp mainApp = new MainApp();
        String responseTest = mainApp.connectByCords("35","139");
        JSONObject jsonObject = new JSONObject(responseTest);
        JSONObject jsonObject1 = jsonObject.getJSONObject("coord");
        Assertions.assertEquals(200,jsonObject.getInt("cod"));
        Assertions.assertEquals(35,jsonObject1.getDouble("lat"));
        Assertions.assertEquals(139, jsonObject1.getDouble("lon"));
    }

    @Test
    @Disabled("Nie została obsłużona")
    void parseJsonForXDaysTest() {
        //TODO
    }
    @AfterAll
    static void done() {
        System.out.println("Zakończenie testów");
    }
}
