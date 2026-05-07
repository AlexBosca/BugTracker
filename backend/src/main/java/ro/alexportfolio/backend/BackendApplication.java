package ro.alexportfolio.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ro.alexportfolio.backend")
public class BackendApplication {

	public static void main(String[] args) {
		// Backend test commit
		SpringApplication.run(BackendApplication.class, args);
	}

}
