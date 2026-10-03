package praktikum.client;

import io.restassured.response.ValidatableResponse;
import praktikum.constants.ApiEndpoints;
import praktikum.models.UserRequest;

import static io.restassured.RestAssured.given;

public class UserClient {
    public ValidatableResponse createUser(UserRequest userRequest) {
        return given()
                .spec(RestClient.getBaseSpec())
                .body(userRequest)
                .when()
                .post(ApiEndpoints.REGISTER)
                .then();
    }

    public ValidatableResponse loginUser(UserRequest userRequest) {
        return given()
                .spec(RestClient.getBaseSpec())
                .body(userRequest)
                .when()
                .post(ApiEndpoints.LOGIN)
                .then();
    }

    public ValidatableResponse deleteUser(String accessToken) {
        return given()
                .spec(RestClient.getAuthSpec(accessToken))
                .when()
                .delete(ApiEndpoints.USER)
                .then();
    }
}
