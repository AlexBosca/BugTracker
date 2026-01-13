package ro.alexportfolio.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ro.alexportfolio.backend")
@SuppressWarnings("java:S1118") // Sonar: Utility class should not have public constructor
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
