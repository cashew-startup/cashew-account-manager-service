package com.cachew.accountmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class AccountManagerApplication {

    public static void main(String[] args) { SpringApplication.run(AccountManagerApplication.class, args); }

}

