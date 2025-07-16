package ru.alexbat.managerapp.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Encoder {

    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("password"));
    }
}
