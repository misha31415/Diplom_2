package praktikum.client;

import io.restassured.response.ValidatableResponse;
import praktikum.constants.ApiEndpoints;
import praktikum.models.OrderRequest;

import static io.restassured.RestAssured.given;

public class OrderClient {
    public ValidatableResponse createOrderWithAuth(OrderRequest orderRequest, String accessToken) {
        return given()
                .spec(RestClient.getAuthSpec(accessToken))
                .body(orderRequest)
                .when()
                .post(ApiEndpoints.ORDERS)
                .then();
    }

    public ValidatableResponse createOrderWithoutAuth(OrderRequest orderRequest) {
        return given()
                .spec(RestClient.getBaseSpec())
                .body(orderRequest)
                .when()
                .post(ApiEndpoints.ORDERS)
                .then();
    }
}
