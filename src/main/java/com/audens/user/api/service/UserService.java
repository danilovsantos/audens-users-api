package com.audens.user.api.service;

import com.audens.user.api.dto.auth.RecoveryJwtTokenDto;
import com.audens.user.api.dto.input.user.CreateUserDto;
import com.audens.user.api.dto.input.user.LoginUserDto;
import com.audens.user.api.dto.output.user.RecoveryUserDto;
import com.audens.user.api.entity.User;
import com.audens.user.api.exceptions.EmailAlreadyExistsException;
import com.audens.user.api.exceptions.UserNotFoundException;
import com.audens.user.api.mappers.UserMapper;
import com.audens.user.api.repository.UserRepository;
import com.audens.user.api.security.authentication.JwtTokenService;
import com.audens.user.api.security.config.SecurityConfiguration;
import com.audens.user.api.security.userdetails.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityConfiguration securityConfiguration;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private UserMapper userMapper;

    public RecoveryJwtTokenDto authenticateUser(LoginUserDto loginUserDto) {
        // Cria um objeto de autenticação com o email e a senha do usuário
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginUserDto.email(), loginUserDto.password());

        // Autentica o usuário com as credenciais fornecidas
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        // Obtém o objeto UserDetails do usuário autenticado
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Gera um token JWT para o usuário autenticado
        return new RecoveryJwtTokenDto(jwtTokenService.generateToken(userDetails));
    }

    public void createUser(CreateUserDto createUserDto) {
        // Verifica se o e-mail fornecido não existe no banco de dados
        if (checkIfEmailNotExists(createUserDto.email())) {

            // Cria um novo usuário com os dados fornecidos
            User newUser = User.builder()
                    .email(createUserDto.email())
                    // Codifica a senha do usuário com o algoritmo
                    .password(securityConfiguration.passwordEncoder().encode(createUserDto.password()))
                    .build();

            // Salva o novo usuário no banco de dados
            userRepository.save(newUser);
        }
    }


    public RecoveryUserDto getUserById(Long userId) {
        return userMapper.mapUserToUserDto(userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new));
    }

    public Page<RecoveryUserDto> getUsers(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        return userPage.map(user -> userMapper.mapUserToUserDto(user));
    }

    public void deleteUserById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException();
        }
        userRepository.deleteById(userId);
    }

    private boolean checkIfEmailNotExists(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException();
        }
        return true;
    }

}
