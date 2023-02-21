package rest_assured;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import configuration.Configuration;

import static io.restassured.RestAssured.given;

public class RestClient {

    public static RequestSpecification getBaseSpec() {
        return given()
                .baseUri(Configuration.getStartUrl())
                .contentType(ContentType.JSON);
    }
}
