package tests;

import models.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.CreateUserSpec.*;

@Tag("API")

public class ReqresTests extends TestBase {

    CreateUserModel createUserModel = new CreateUserModel();
    CreateUserNameJobModel createUserNameJobModel = new CreateUserNameJobModel();
    UpdateSuccessfulModel updateSuccessfulModel = new UpdateSuccessfulModel();

    @Test
    public void SuccessfulRegTest() {
        createUserModel.setEmail("eve.holt@reqres.in");
        createUserModel.setPassword("pistol");
        CreateUserResponseModel response = step("Успешное создание пользователя", () ->
                given(createUserRequestSpec)
                        .body(createUserModel)
                        .when()
                        .post("/register")
                        .then()
                        .spec(createUserResponseSpec)
                        .extract().as(CreateUserResponseModel.class));
        step("Проверка создания пользователя по id, токену", () -> {
            assertEquals(4, response.getId());
            assertEquals("QpwL5tke4Pnpja7X4", response.getToken());
        });
    }

    @Test
    public void unSuccessfulRegTest() {
        createUserModel.setEmail("sydney@fife");
        CreateUserResponseModel response = step("Неуспешное создание пользователя", () ->
                given(createUserRequestSpec)
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
    public void CreateUserNameJobTest() {
        createUserNameJobModel.setName("morpheus");
        createUserNameJobModel.setJob("leader");
        CreateUserNameJobResponseModel response = step("Создание пользователя с именем и работой", () ->
                given(createUserNameJobRequestSpec)
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
        updateSuccessfulModel.setJob("zion resident");
        UpdateSuccessfulResponseModel response = step("Изменение работы у пользователя", () ->
                given(updateSuccessfulRequestSpec)
                        .body(updateSuccessfulModel)
                        .when()
                        .patch("/users/2")
                        .then()
                        .spec(updateSuccessfulRequestSpec200)
                        .extract().as(UpdateSuccessfulResponseModel.class));
        step("Проверка изменения работы у пользователя", () -> {
            assertEquals("zion resident", response.getJob());
        });
    }

    @Test
    void updateSuccessfulNameTest() {
        updateSuccessfulModel.setName("morpheus");
        UpdateSuccessfulResponseModel response = step("Изменение имени у пользователя", () ->
                given(updateSuccessfulRequestSpec)
                        .body(updateSuccessfulModel)
                        .when()
                        .patch("/users/2")
                        .then()
                        .spec(updateSuccessfulRequestSpec200)
                        .extract().as(UpdateSuccessfulResponseModel.class));
        step("Проверка изменения имени у пользователя", () -> {
            assertEquals("morpheus", response.getName());
        });
    }
}

