package praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.models.OrderRequest;
import praktikum.models.UserRequest;
import praktikum.steps.OrderSteps;
import praktikum.steps.UserSteps;
import praktikum.utils.StringRandomUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;

public class CreateOrderTest extends BaseTest {
    private UserSteps userSteps;
    private OrderSteps orderSteps;
    private UserRequest userRequest;
    private String accessToken;
    private String validIngredient1;
    private String validIngredient2;
    private static final String INVALID_INGREDIENT = "invalidHash";

    @Before
    public void setUp() {
        userSteps = new UserSteps();
        orderSteps = new OrderSteps();

        userRequest = new UserRequest(
                StringRandomUtils.getRandomEmail(),
                StringRandomUtils.getRandomPassword(),
                StringRandomUtils.getRandomName()
        );

        var createResponse = userSteps.createUser(userRequest);
        userSteps.checkUserSuccessResponse(createResponse, userRequest);
        accessToken = userSteps.getAccessToken(createResponse);
        List<String> ingredientIds = orderSteps.getIngredientIds();
        validIngredient1 = ingredientIds.get(0);
        validIngredient2 = ingredientIds.get(1);
    }

    @After
    public void tearDown() {
        if (accessToken != null && !accessToken.isEmpty()) {
            userSteps.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    @Description("Проверка успешного создания заказа авторизованным пользователем")
    public void createOrderWithAuthTest() {

        List<String> ingredients = Arrays.asList(validIngredient1, validIngredient2);
        OrderRequest orderRequest = new OrderRequest(ingredients);

        var response = orderSteps.createOrderWithAuth(orderRequest, accessToken);
        orderSteps.checkOrderCreated(response);
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Проверка успешного создания заказа неавторизованным пользователем")
    public void createOrderWithoutAuthTest() {
        List<String> ingredients = Arrays.asList(validIngredient1, validIngredient2);
        OrderRequest orderRequest = new OrderRequest(ingredients);

        var response = orderSteps.createOrderWithoutAuth(orderRequest);
        orderSteps.checkOrderCreated(response);
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами")
    @Description("Проверка успешного создания заказа с корректными ингредиентами")
    public void createOrderWithIngredientsTest() {
        List<String> ingredients = Arrays.asList(validIngredient1, validIngredient2);
        OrderRequest orderRequest = new OrderRequest(ingredients);

        var response = orderSteps.createOrderWithAuth(orderRequest, accessToken);
        orderSteps.checkOrderCreated(response);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Проверка ошибки при создании заказа без ингредиентов")
    public void createOrderWithoutIngredientsTest() {
        OrderRequest orderRequest = new OrderRequest(Collections.emptyList());

        var response = orderSteps.createOrderWithAuth(orderRequest, accessToken);
        orderSteps.checkOrderError(response, SC_BAD_REQUEST, "Ingredient ids must be provided");
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиента")
    @Description("Проверка ошибки при создании заказа с невалидным хешем ингредиента")
    public void createOrderWithInvalidIngredientHashTest() {
        List<String> ingredients = Arrays.asList(INVALID_INGREDIENT, validIngredient2);
        OrderRequest orderRequest = new OrderRequest(ingredients);

        var response = orderSteps.createOrderWithAuth(orderRequest, accessToken);
        orderSteps.checkOrderInternalServerError(response);
    }
}
