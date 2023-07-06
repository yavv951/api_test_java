import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;


public class CurrentWeatherDataTest {
    private final String API_KEY = "1d21135a27e57eca76380a6e45032abb";
    private final String baseURI = "https://api.openweathermap.org/data/2.5/weather";

    @Test
    @Description("Test the GET request to retrieve weather information by city name")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Positive test: get weather by city name")
    public void testGetCurrentWeatherData() {
        given()
                .param("q", "London")
                .param("appid", API_KEY)
                .when()
                .get(baseURI)
                .then()
                .statusCode(200)
                .body("name", equalTo("London"))
                .body("weather[0].description", containsString("clouds"));

    }
    @Test
    @Description("Test the GET request to retrieve weather information by geographic coordinates")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get current weather by geographic coordinates")
    public void testGetWeatherByCoordinates() {
        given()
                .param("lat", "55.75")
                .param("lon", "37.62")
                .param("appid", API_KEY)
                .when()
                .get(baseURI)
                .then()
                .statusCode(200)
                .body("name", equalTo("Moscow"));
    }

    @Test
    @Description("Test the GET request to retrieve weather information by city ID")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get current weather by city ID")
    public void testGetWeatherByCityId() {
        //Получение погоды по ID города:
        given()
                .param("id", "2643743")
                .param("appid", API_KEY)
                .when()
                .get(baseURI)
                .then()
                .statusCode(200)
                .body("name", equalTo("London"));
    }

    @Test
    @Description("Сhecking the successful receipt of data when requesting the weather in the current city using " +
            "a zip code")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get current weather by zip code")
    public void testGetCurrentWeatherDataByZipCode() {
        Response response = given()
                .queryParam("zip", "10001,us")
                .queryParam("appid", API_KEY)
                .when()
                .get(baseURI)
                .then()
                .assertThat()
                .statusCode(200)
                .body("name", equalTo("New York"))
                .extract().response();

        String jsonResponseAsString = response.asString();
        System.out.println(jsonResponseAsString);
    }
    @Test
    @Description("Test the GET request to retrieve weather information by city Invalid API KEY")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get current weather by city Invalid API KEY")
    public void testGetCurrentWeatherDataWithInvalidAppId() {
        //Отправка запроса с неверным API-ключом:
        given()
                .param("q", "London")
                .param("appid", "invalid_app_id")
                .when()
                .get(baseURI)
                .then()
                .statusCode(401);
    }

    @Test
    @Description("Test the GET request to retrieve weather information by Invalid city ")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get current weather by Invalid city")
    public void testGetCurrentWeatherDataWithInvalidCityName() {
        //Отправка запроса с неверным названием города:
        given()
                .param("q", "InvalidCityName")
                .param("appid", API_KEY)
                .when()
                .get(baseURI)
                .then()
                .statusCode(404);
    }

    @Test
    @Description("Test the GET request to retrieve weather information by city None API KEY")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get current weather by city None API KEY")
    public void testGetCurrentWeatherDataWithMissingAppId() {
        //Отправка запроса без API-ключа:
        given()
                .param("q", "London")
                .when()
                .get(baseURI)
                .then()
                .statusCode(401);
    }
    @Test
    @Description("Test the GET request to retrieve weather information by city invalid URL")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get current weather by city invalid URL")
    public void testGetCurrentWeatherDataWithMissingUrl() {
        //Отправка запроса с неверным URL
        given()
                .param("q", "London")
                .param("appid", API_KEY)
                .when()
                .get(baseURI+1)
                .then()
                .statusCode(404);
    }
}
