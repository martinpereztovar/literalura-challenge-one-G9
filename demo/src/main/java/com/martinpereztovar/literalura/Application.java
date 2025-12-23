package com.martinpereztovar.literalura;

import com.martinpereztovar.literalura.menu.Menu;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private final Menu menu;

    public Application(Menu menu) {
        this.menu = menu;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {
        menu.showMenu();
    }
}
