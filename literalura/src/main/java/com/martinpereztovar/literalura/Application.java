package com.martinpereztovar.literalura;

import com.martinpereztovar.literalura.menu.Menu;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private final Menu menu;
    private final JdbcTemplate jdbc;

    public Application(Menu menu, JdbcTemplate jdbc) {
        this.menu = menu;
        this.jdbc = jdbc;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {
        var info = jdbc.queryForMap("select current_database() db, current_user usr, current_schema() sch");
        System.out.println("DB CHECK => " + info);

        menu.showMenu();
    }
}
