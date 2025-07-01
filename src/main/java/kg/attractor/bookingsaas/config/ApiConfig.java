package kg.attractor.bookingsaas.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableCaching
public class ApiConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("https://date.nager.at/api/v3/PublicHolidays")
                .build();
    }
}
