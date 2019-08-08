package io.aktivator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Configuration
public class TestRunConfiguration {

    public static final String SIMPLE_USER = "simpleuser";
    public static final String ACTIVIST_USER = "akivistuser";
    public static final String DUMMY_PASS = "pass1234567!";
    private static final Logger LOGGER = LoggerFactory.getLogger(TestRunConfiguration.class);
    @Autowired
    private WebClientToken webClientToken;

    @PostConstruct
    public void afterStart() {
        LOGGER.info("Setting up user for testing.");
        webClientToken.createUser(SIMPLE_USER, DUMMY_PASS, "johndone@gmail.com", "John", "Doe", "user");
        webClientToken.createUser(ACTIVIST_USER, DUMMY_PASS, "johnnybravo@gmail.com", "Johnny", "Bravo", "activist");
    }

    @PreDestroy
    public void onExit() {
        LOGGER.info("removing users created for testing.");
        webClientToken.removeUser(SIMPLE_USER);
        webClientToken.removeUser(ACTIVIST_USER);
    }

}
