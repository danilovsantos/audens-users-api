package com.audens.user.api.config;

import com.audens.user.api.dto.input.user.CreateUserDto;
import com.audens.user.api.repository.UserRepository;
import com.audens.user.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationRunnerImpl implements ApplicationRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private static final String NAME = "Admin";

    private static final String EMAIL = "admin@email.com";

    private static final String PASSWORD = "123456789";


    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            createAdminUser();
        } catch(Exception e) {
            throw new Exception("Erro ao criar usuário", e);
        }
    }

    private void createAdminUser() {
        if (userRepository.findByEmail(EMAIL).isEmpty()) {
            userService.createUser(new CreateUserDto(NAME, EMAIL, PASSWORD));
        }
    }
}
