package praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import praktikum.client.OrderClient;
import praktikum.client.RestClient;
import praktikum.constants.ApiEndpoints;
import praktikum.models.OrderRequest;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertTrue;

public class OrderSteps {
    private final OrderClient orderClient;

    public OrderSteps() {
        this.orderClient = new OrderClient();
    }

    @Step("Создание заказа с авторизацией")
    public ValidatableResponse createOrderWithAuth(OrderRequest orderRequest, String accessToken) {
        return orderClient.createOrderWithAuth(orderRequest, accessToken);
    }

    @Step("Создание заказа без авторизации")
    public ValidatableResponse createOrderWithoutAuth(OrderRequest orderRequest) {
        return orderClient.createOrderWithoutAuth(orderRequest);
    }

    @Step("Получение списка ингредиентов")
    public List<String> getIngredientIds() {
        return given()
                .spec(RestClient.getBaseSpec())
                .when()
                .get(ApiEndpoints.INGREDIENTS)
                .then()
                .statusCode(SC_OK)
                .extract()
                .jsonPath()
                .getList("data._id", String.class);
    }

    @Step("Проверка успешного создания заказа")
    public void checkOrderCreated(ValidatableResponse response) {
        response.statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());
    }

    @Step("Проверка ошибки при создании заказа: {expectedMessage}")
    public void checkOrderError(ValidatableResponse response, int expectedStatusCode, String expectedMessage) {
        response.statusCode(expectedStatusCode)
                .body("success", equalTo(false))
                .body("message", equalTo(expectedMessage));
    }

    @Step("Проверка ошибки 500 при создании заказа с невалидным хешем")
    public void checkOrderInternalServerError(ValidatableResponse response) {
        response.statusCode(SC_INTERNAL_SERVER_ERROR);
        String responseBody = response.extract().asString();
        assertTrue("Ответ должен содержать ошибку", responseBody.contains("Internal Server Error"));
    }

}
