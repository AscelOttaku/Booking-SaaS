package kg.attractor.bookingsaas.integration.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@Sql(scripts = {
        "/test-data/roles_users.sql",
        "/test-data/businesses.sql",
        "/test-data/business_reviews.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class BusinessReviewApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllReviews() throws Exception {
        mockMvc.perform(get("/api/business/reviews")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].businessName").value("Test Business"))
                .andExpect(jsonPath("$.content[0].averageRating").value(4.0))
                .andExpect(jsonPath("$.content[0].reviewCount").value(3));
    }
}