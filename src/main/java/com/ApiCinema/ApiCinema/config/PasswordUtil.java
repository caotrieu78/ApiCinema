package com.ApiCinema.ApiCinema.config;

import org.mindrot.jbcrypt.BCrypt;


import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
