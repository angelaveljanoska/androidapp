package com.example.zivototekrug.utility;

public class RegisterDto {
    public String name;
    public String surname;
    public String phone;
    public String role;

    public RegisterDto(String name, String surname, String phone, String role) {
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.role = role;
    }
}
