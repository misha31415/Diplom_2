package praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import praktikum.client.UserClient;
import praktikum.models.UserRequest;
import praktikum.models.UserResponse;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;


public class UserSteps {
    private final UserClient userClient;

    public UserSteps() {
        this.userClient = new UserClient();
    }

    @Step("Создание нового пользователя: {userRequest.email}")
    public ValidatableResponse createUser(UserRequest userRequest) {
        return userClient.createUser(userRequest);
    }

    @Step("Авторизация пользователя: {userRequest.email}")
    public ValidatableResponse loginUser(UserRequest userRequest) {
        return userClient.loginUser(userRequest);
    }

    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(String accessToken) {
        return userClient.deleteUser(accessToken);
    }

    @Step("Проверка успешного ответа пользователя")
    public void checkUserSuccessResponse(ValidatableResponse response, UserRequest userRequest) {
        response.statusCode(SC_OK);

        UserResponse userResponse = response.extract().as(UserResponse.class);
        assertTrue("Ответ должен быть успешным", userResponse.isSuccess());
        assertNotNull("AccessToken не должен быть null", userResponse.getAccessToken());
        assertNotNull("RefreshToken не должен быть null", userResponse.getRefreshToken());
        assertEquals("Email должен совпадать", userRequest.getEmail(), userResponse.getUser().getEmail());
        assertEquals("Имя должно совпадать", userRequest.getName(), userResponse.getUser().getName());
    }

    @Step("Проверка успешной авторизации пользователя")
    public void checkUserLoggedIn(ValidatableResponse response, UserRequest userRequest) {
        response.statusCode(SC_OK);

        UserResponse userResponse = response.extract().as(UserResponse.class);
        assertTrue("Пользователь должен быть авторизован", userResponse.isSuccess());
        assertNotNull("AccessToken не должен быть null", userResponse.getAccessToken());
        assertNotNull("RefreshToken не должен быть null", userResponse.getRefreshToken());
        assertEquals("Email должен совпадать", userRequest.getEmail(), userResponse.getUser().getEmail());
        assertEquals("Имя должно совпадать", userRequest.getName(), userResponse.getUser().getName());
    }

    @Step("Проверка ошибки при создании пользователя: {expectedMessage}")
    public void checkUserCreationError(ValidatableResponse response, int expectedStatusCode, String expectedMessage) {
        response.statusCode(expectedStatusCode)
                .body("success", equalTo(false))
                .body("message", equalTo(expectedMessage));
    }

    @Step("Проверка ошибки при авторизации")
    public void checkLoginError(ValidatableResponse response, int expectedStatusCode, String expectedMessage) {
        response.statusCode(expectedStatusCode)
                .body("success", equalTo(false))
                .body("message", equalTo(expectedMessage));
    }

    @Step("Получение accessToken из ответа")
    public String getAccessToken(ValidatableResponse response) {
        return response.extract().as(UserResponse.class).getAccessToken();
    }
}
