package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.models.UserRequest;
import praktikum.steps.UserSteps;
import praktikum.utils.StringRandomUtils;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

public class LoginUserTest extends BaseTest{
    private UserSteps userSteps;
    private UserRequest userRequest;
    private String accessToken;

    @Before
    public void setUp() {
        userSteps = new UserSteps();

        userRequest = new UserRequest(
                StringRandomUtils.getRandomEmail(),
                StringRandomUtils.getRandomPassword(),
                StringRandomUtils.getRandomName()
        );

        var createResponse = userSteps.createUser(userRequest);
        userSteps.checkUserSuccessResponse(createResponse, userRequest);
        accessToken = userSteps.getAccessToken(createResponse);
    }

    @After
    public void tearDown() {
        if (accessToken != null && !accessToken.isEmpty()) {
            userSteps.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Вход под существующим пользователем")
    @Description("Проверка успешного входа пользователя с корректными данными")
    public void loginExistingUserTest() {
        var response = userSteps.loginUser(userRequest);
        userSteps.checkUserLoggedIn(response, userRequest);
    }

    @Test
    @DisplayName("Вход с неверным логином и паролем")
    @Description("Проверка ошибки при входе с неверными учётными данными")
    public void loginWithInvalidCredentialsTest() {
        UserRequest invalidUserRequest = new UserRequest(
                "invalid@email.com",
                "invalidPassword",
                "InvalidName"
        );

        var response = userSteps.loginUser(invalidUserRequest);
        userSteps.checkLoginError(response, SC_UNAUTHORIZED, "email or password are incorrect");
    }

    @Test
    @DisplayName("Вход с неверным паролем")
    @Description("Проверка ошибки при входе с неверным паролем")
    public void loginWithInvalidPasswordTest() {
        UserRequest invalidUserRequest = new UserRequest(
                userRequest.getEmail(),
                "wrongPassword",
                userRequest.getName()
        );

        var response = userSteps.loginUser(invalidUserRequest);
        userSteps.checkLoginError(response, SC_UNAUTHORIZED, "email or password are incorrect");
    }

    @Test
    @DisplayName("Вход с неверным email")
    @Description("Проверка ошибки при входе с неверным email")
    public void loginWithInvalidEmailTest() {
        UserRequest invalidUserRequest = new UserRequest(
                "wrong@" + userRequest.getEmail(),
                userRequest.getPassword(),
                userRequest.getName()
        );

        var response = userSteps.loginUser(invalidUserRequest);
        userSteps.checkLoginError(response, SC_UNAUTHORIZED, "email or password are incorrect");
    }
}
