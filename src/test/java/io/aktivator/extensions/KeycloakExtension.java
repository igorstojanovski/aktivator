package io.aktivator.extensions;

import io.aktivator.WebClientToken;
import io.aktivator.profile.TestParameter;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.springframework.stereotype.Component;

import java.lang.reflect.Parameter;
import java.util.Random;

@Component
public class KeycloakExtension implements BeforeAllCallback, AfterAllCallback, ParameterResolver {
    private static final Random RANDOM = new Random();
    private static final String DUMMY_PASS = "pass1234567!";
    private static final WebClientToken WEB_CLIENT_TOKEN = new WebClientToken();
    private String adminUser;
    private String simpleUser;

    @Override
    public void afterAll(ExtensionContext extensionContext) {
        WEB_CLIENT_TOKEN.removeUser(adminUser);
        WEB_CLIENT_TOKEN.removeUser(simpleUser);
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        adminUser = "admin" + RANDOM.nextInt(1000);
        simpleUser = "simple" + RANDOM.nextInt(1000);
        WEB_CLIENT_TOKEN.createUser(adminUser, DUMMY_PASS, "johndone@gmail.com", "John", "Doe", "user");
        WEB_CLIENT_TOKEN.createUser(simpleUser, DUMMY_PASS, "johnnybravo@gmail.com", "Johnny", "Bravo", "activist");
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Class<?> type = parameterContext.getParameter().getType();
        return type == WebClientToken.class || type == String.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Parameter parameter = parameterContext.getParameter();
        if (parameter.getType() == WebClientToken.class) {
            return WEB_CLIENT_TOKEN;
        }
        String value = parameterContext.findAnnotation(TestParameter.class).get().value();
        if ("user_token".equals(value)) {
            return WEB_CLIENT_TOKEN.getToken(simpleUser, DUMMY_PASS);
        } else {
            return WEB_CLIENT_TOKEN.getToken(adminUser, DUMMY_PASS);
        }

    }
}
