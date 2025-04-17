package com.armacare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class ArmaCareApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load(); // Charge le fichier .env
        System.setProperty("MYSQL_USER", dotenv.get("MYSQL_USER"));
        System.setProperty("MYSQL_PASSWORD", dotenv.get("MYSQL_PASSWORD"));
        System.setProperty("MYSQL_HOST", dotenv.get("MYSQL_HOST"));
        System.setProperty("MYSQL_DATABASE", dotenv.get("MYSQL_DATABASE"));
		SpringApplication.run(ArmaCareApplication.class, args);
	}

}
