package tech.finaya.wallet.integration;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import tech.finaya.wallet.domain.usecases.CreateKey;
import tech.finaya.wallet.domain.usecases.CreateUser;
import tech.finaya.wallet.domain.usecases.MakePixOut;
import tech.finaya.wallet.domain.usecases.GetBalance;
import tech.finaya.wallet.domain.usecases.MakeDeposit;
import tech.finaya.wallet.integration.config.TestContainersConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestContainersConfig.class)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
public class BaseIT {

    @LocalServerPort
    protected int port;

    protected WebTestClient webTestClient;

    protected ObjectMapper objectMapper;

    protected CreateUser createUser;
    protected CreateKey createKey;
    protected MakeDeposit makeDeposit;
    protected MakePixOut makePixOut;
    protected GetBalance getBalance;

    @Autowired
    protected ApplicationContext applicationContext;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUpWebTestClient() {
        this.webTestClient = WebTestClient.bindToServer()
            .baseUrl("http://localhost:" + port)
            .build();

        objectMapper = applicationContext.getBean(ObjectMapper.class);

        createUser = applicationContext.getBean(CreateUser.class);
        createKey = applicationContext.getBean(CreateKey.class);
        makeDeposit = applicationContext.getBean(MakeDeposit.class);
        makePixOut = applicationContext.getBean(MakePixOut.class);
        getBalance = applicationContext.getBean(GetBalance.class);

        jdbcTemplate.execute("TRUNCATE TABLE users RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE wallets RESTART IDENTITY CASCADE");
    }
    
}
