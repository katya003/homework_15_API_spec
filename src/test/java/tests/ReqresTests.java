package tests;

import models.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static specs.CreateUserSpec.*;

@Tag("API")

public class ReqresTests extends TestBase {

    CreateUserModel createUserModel = new CreateUserModel();
    CreateUserNameJobModel createUserNameJobModel = new CreateUserNameJobModel();

    @Test
    public void successfulRegTest() {
        createUserModel.setEmail("eve.holt@reqres.in");
        createUserModel.setPassword("pistol");
        CreateUserResponseModel response = step("Успешное создание пользователя", () ->
                given(createRequestSpec)
                        .body(createUserModel)
                        .when()
                        .post("/register")
                        .then()
                        .spec(successfulUserResponseSpec)
                        .extract().as(CreateUserResponseModel.class));
        step("Проверка создания пользователя по id, токену", () -> {
            assertEquals(4, response.getId());
            assertNotNull(response.getToken(), "Токен должен быть не null");
            assertEquals(17, response.getToken().length(), "Длина токена должна быть 17 символов");
        });
    }

    @Test
    public void unSuccessfulRegTest() {
        createUserModel.setEmail("sydney@fife");
        CreateUserResponseModel response = step("Неуспешное создание пользователя", () ->
                given(createRequestSpec)
                        .body(createUserModel)
                        .when()
                        .post("/register")
                        .then()
                        .spec(createUserResponseSpec400)
                        .extract().as(CreateUserResponseModel.class));
        step("Проверка ошибки при создании пользователя", () -> {
            assertEquals("Missing password", response.getError());
        });
    }

    @Test
    public void createUserNameJobTest() {
        createUserNameJobModel.setName("morpheus");
        createUserNameJobModel.setJob("leader");
        CreateUserNameJobResponseModel response = step("Создание пользователя с именем и работой", () ->
                given(createRequestSpec)
                        .body(createUserNameJobModel)
                        .when()
                        .post("/user")
                        .then()
                        .spec(createNameJobResponseSpec201)
                        .extract().as(CreateUserNameJobResponseModel.class));
        step("Проверка создания пользователя с именем и работой", () -> {
            assertEquals("morpheus", response.getName());
            assertEquals("leader", response.getJob());
        });
    }

    @Test
    public void updateSuccessfulJobTest() {
        createUserNameJobModel.setJob("zion resident");
        UpdateSuccessfulResponseModel response = step("Изменение работы у пользователя", () ->
                given(updateSuccessfulRequestSpec)
                        .body(createUserNameJobModel)
                        .when()
                        .patch("/users/2")
                        .then()
                        .spec(successfulUserResponseSpec)
                        .extract().as(UpdateSuccessfulResponseModel.class));
        step("Проверка изменения работы у пользователя", () -> {
            assertEquals("zion resident", response.getJob());
        });
    }

    @Test
    void updateSuccessfulNameTest() {
        createUserNameJobModel.setName("morpheus");
        UpdateSuccessfulResponseModel response = step("Изменение имени у пользователя", () ->
                given(updateSuccessfulRequestSpec)
                        .body(createUserNameJobModel)
                        .when()
                        .patch("/users/2")
                        .then()
                        .spec(successfulUserResponseSpec)
                        .extract().as(UpdateSuccessfulResponseModel.class));
        step("Проверка изменения имени у пользователя", () -> {
            assertEquals("morpheus", response.getName());
        });
    }
}

