package ro.alexportfolio.backend.config;

import java.time.Clock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SuppressWarnings("java:S1118")
@Configuration
public class ClockConfiguration {

    @Bean
    public static Clock clock() {
        return Clock.systemDefaultZone();
    }
}
