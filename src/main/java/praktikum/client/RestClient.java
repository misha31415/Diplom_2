package praktikum.client;
import praktikum.constants.ApiEndpoints;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class RestClient {

    public static RequestSpecification getBaseSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(ApiEndpoints.BASE_URL)
                .setContentType(ContentType.JSON)
                .build();
    }

    public static RequestSpecification getAuthSpec(String accessToken) {
        return new RequestSpecBuilder()
                .setBaseUri(ApiEndpoints.BASE_URL)
                .setContentType(ContentType.JSON)
                .addHeader("Authorization", accessToken)
                .build();
    }
}
