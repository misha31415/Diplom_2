package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.models.UserRequest;
import praktikum.steps.UserSteps;
import praktikum.utils.StringRandomUtils;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;

public class CreateUserTest extends BaseTest {
    private UserSteps userSteps;
    private UserRequest userRequest;
    private String accessToken;

    @Before
    public void setUp() {
        userSteps = new UserSteps();
    }

    @After
    public void tearDown() {
        if (accessToken != null && !accessToken.isEmpty()) {
            userSteps.deleteUser(accessToken);
        }
    }

    private UserRequest createRandomUserRequest() {
        return new UserRequest(
                StringRandomUtils.getRandomEmail(),
                StringRandomUtils.getRandomPassword(),
                StringRandomUtils.getRandomName()
        );
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("Проверка успешного создания нового уникального пользователя")
    public void createUniqueUserTest() {
        userRequest = createRandomUserRequest();

        var response = userSteps.createUser(userRequest);

        userSteps.checkUserSuccessResponse(response, userRequest);
        accessToken = userSteps.getAccessToken(response);
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    @Description("Проверка ошибки при попытке создать пользователя с уже существующими данными")
    public void createExistingUserTest() {
        userRequest = createRandomUserRequest();

        var firstResponse = userSteps.createUser(userRequest);
        userSteps.checkUserSuccessResponse(firstResponse, userRequest);
        accessToken = userSteps.getAccessToken(firstResponse);

        var response = userSteps.createUser(userRequest);

        userSteps.checkUserCreationError(response, SC_FORBIDDEN, "User already exists");
    }

    @Test
    @DisplayName("Создание пользователя без заполнения обязательного поля (email)")
    @Description("Проверка ошибки при создании пользователя без email")
    public void createUserWithoutEmailTest() {
        userRequest = new UserRequest(null, StringRandomUtils.getRandomPassword(), StringRandomUtils.getRandomName());

        var response = userSteps.createUser(userRequest);

        userSteps.checkUserCreationError(response, SC_FORBIDDEN, "Email, password and name are required fields");
    }

    @Test
    @DisplayName("Создание пользователя без заполнения обязательного поля (password)")
    @Description("Проверка ошибки при создании пользователя без password")
    public void createUserWithoutPasswordTest() {
        userRequest = new UserRequest(StringRandomUtils.getRandomEmail(), null, StringRandomUtils.getRandomName());

        var response = userSteps.createUser(userRequest);

        userSteps.checkUserCreationError(response, SC_FORBIDDEN, "Email, password and name are required fields");
    }

    @Test
    @DisplayName("Создание пользователя без заполнения обязательного поля (name)")
    @Description("Проверка ошибки при создании пользователя без name")
    public void createUserWithoutNameTest() {
        userRequest = new UserRequest(StringRandomUtils.getRandomEmail(), StringRandomUtils.getRandomPassword(), null);

        var response = userSteps.createUser(userRequest);

        userSteps.checkUserCreationError(response, SC_FORBIDDEN, "Email, password and name are required fields");
    }
}
