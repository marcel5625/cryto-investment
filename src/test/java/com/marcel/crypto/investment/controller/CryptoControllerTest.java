package com.marcel.crypto.investment.controller;

import com.marcel.crypto.investment.helper.MongoDbContainer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Path;

import static com.marcel.crypto.investment.helper.JsonUtil.forJson;
import static java.nio.file.Files.readString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.skyscreamer.jsonassert.JSONCompareMode.STRICT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ContextConfiguration(initializers = CryptoControllerTest.MongoDbInitializer.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class CryptoControllerTest {

    @Autowired
    MockMvc mockMvc;

    static MongoDbContainer mongoDbContainer;

    @BeforeAll
    public static void startContainerAndPublicPortIsAvailable() {
        mongoDbContainer = new MongoDbContainer();
        mongoDbContainer.start();
    }

    @AfterAll
    public static void stopContainer() {
        if (mongoDbContainer.isRunning()) {
            mongoDbContainer.stop();
        }
    }

    @Test
    void getNormalizeCryptos() throws Exception {
        var result = mockMvc.perform(get("/v1/cryptos/normalize/{year}/{month}", 2022, 1))
                .andExpect(status().isOk());

        assertThat(forJson(result.andReturn().getResponse().getContentAsString()))
                .isEqualToJson(readString(Path.of("src/test/resources/payloads/monthly-normalize-crytos.json")),
                        new CustomComparator(STRICT, new Customization("[*].id", (c1, c2) -> true)));
    }

    @Test
    void getNormalizeCrypto_empty() throws Exception {
        var result = mockMvc.perform(get("/v1/cryptos/normalize/{year}/{month}", 2022, 2))
                .andExpect(status().isOk());

        assertThat(forJson(result.andReturn().getResponse().getContentAsString()))
                .isEqualToJson(readString(Path.of("src/test/resources/payloads/monthly-normalize-crytos-empty.json")), STRICT);

    }

    @Test
    void getMonthlyCryptoInformation() throws Exception {
        var result = mockMvc.perform(get("/v1/cryptos/{crypto}/{year}/{month}", "ETH", 2022, 1))
                .andExpect(status().isOk());

        assertThat(forJson(result.andReturn().getResponse().getContentAsString()))
                .isEqualToJson(readString(Path.of("src/test/resources/payloads/monthly-cryto-information.json")),
                        new CustomComparator(STRICT, new Customization("id", (c1, c2) -> true)));
    }

    @Test
    void getMonthlyCryptoInformation_empty() throws Exception {
        mockMvc.perform(get("/v1/cryptos/{crypto}/{year}/{month}", "WR", 2022, 1))
                .andExpect(status().isNotFound());
    }

    @Test
    void getDailyNormalizeCrypto() throws Exception {
        var result = mockMvc.perform(get("/v1/cryptos/normalize/highest/{date}", "2022-01-01"))
                .andExpect(status().isOk());

        assertThat(forJson(result.andReturn().getResponse().getContentAsString()))
                .isEqualToJson(readString(Path.of("src/test/resources/payloads/daily-highest-crypto.json")),
                        new CustomComparator(STRICT, new Customization("id", (c1, c2) -> true)));
    }

    @Test
    void getDailyNormalizeCrypto_empty() throws Exception {
        mockMvc.perform(get("/v1/cryptos/normalize/highest/{date}", "2022-12-12"))
                .andExpect(status().isNotFound());
    }

    public static class MongoDbInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(@NotNull ConfigurableApplicationContext configurableApplicationContext) {

            TestPropertyValues values = TestPropertyValues.of(
                    "spring.data.mongodb.host=" + mongoDbContainer.getHost(),
                    "spring.data.mongodb.port=" + mongoDbContainer.getPort()

            );
            values.applyTo(configurableApplicationContext);
        }
    }
}