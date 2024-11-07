package models;

import lombok.Data;

@Data
public class CreateUserResponseModel {
    String token, error;
    Integer id;
}