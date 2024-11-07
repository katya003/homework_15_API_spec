package models;

import lombok.Data;

@Data
public class CreateUserModel {
    String email, password;
}