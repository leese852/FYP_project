package com.leese.usercenter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class UserCenterApplicationTests {

    @Test
    void testDigest() {
        String password = "1234";
        BCryptPasswordEncoder encryptPassword = new BCryptPasswordEncoder();
        String encode1 = encryptPassword.encode(password);
        System.out.println(encode1);

        String encode2 = encryptPassword.encode(password);
        System.out.println(encode2);

        boolean matches1 = encryptPassword.matches(password, encode1);
        System.out.println("matches1:" + matches1);

        boolean matches2 = encryptPassword.matches(password, encode2);
        System.out.println("matches2:" + matches2);
    }

}
