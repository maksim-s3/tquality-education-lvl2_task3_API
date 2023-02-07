package rest_assured;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;

public class RestClient {
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    public static RequestSpecification getBaseSpec(){
        return given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON);
    }
}
