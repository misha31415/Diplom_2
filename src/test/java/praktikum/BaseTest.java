package praktikum;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.Before;

public class BaseTest {
    @Before
    public void setUpAllure() {
        RestAssured.filters(new AllureRestAssured());
    }
}
