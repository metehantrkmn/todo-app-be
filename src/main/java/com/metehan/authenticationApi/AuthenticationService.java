package com.metehan.authenticationApi;

import com.metehan.authenticationApi.dto.AuthenticationRequest;
import com.metehan.authenticationApi.dto.AuthenticationResponse;
import com.metehan.authenticationApi.dto.RegisterRequest;
import com.metehan.authenticationApi.token.Token;
import com.metehan.authenticationApi.token.TokenRepository;
import com.metehan.authenticationApi.token.TokenType;
import com.metehan.config.JwtService;
import com.metehan.authenticationApi.exception.UserAlreadyExistException;
import com.metehan.authenticationApi.user.Role;
import com.metehan.authenticationApi.user.User;
import com.metehan.authenticationApi.user.UserRepository;
import com.metehan.todoApi.todo.TodoRepository;
import com.metehan.todoApi.todo.UserDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final TodoRepository todoRepository;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;


    public AuthenticationResponse register(RegisterRequest request){

        //throw exception if user already exists
        if(userRepository.existsUserByEmail(request.getEmail()))
            throw(new UserAlreadyExistException("User Already exists"));

        //generate and save user object
        User user = User.builder()
                        .role(Role.USER)
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .lastname(request.getLastname())
                        .username(request.getUsername())
                        .build();
        var savedUser = userRepository.save(user);

        //generate a docuemnt for the provided email
        UserDocument newDocument = UserDocument.builder()
                .email(request.getEmail())
                .todos(null)
                .build();
        todoRepository.insert(newDocument);

        //generate token string using jwt service
        var jwtToken = jwtService.generateToken(user);

        //create token object and save it to the database
        saveUserToken(savedUser, jwtToken);

        //create response
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private void saveUserToken(User savedUser, String jwtToken) {
        Token token = Token.builder()
                .expired(false)
                .user(savedUser)
                .revoked(false)
                .tokenType(TokenType.BEARER)
                .token(jwtToken)
                .build();
        tokenRepository.save(token);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword())
        );
        User user = userRepository.findUserByEmail(request.getEmail()).orElseThrow();
        var token = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user,token);
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

    private void revokeAllUserTokens(User user){
        List<Token> validUserTokens = tokenRepository.findValidTokensById(user.getId());
        if(validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(t -> {
            t.setRevoked(true);
            t.setExpired(true);
        });
        //save or save all methods updates the table if entities are already exists, add new entity if there is not
        tokenRepository.saveAll(validUserTokens);
    }


}
