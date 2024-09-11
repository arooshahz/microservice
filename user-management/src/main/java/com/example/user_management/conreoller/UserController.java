package com.example.user_management.conreoller;

import com.example.user_management.config.KeycloakProvider;
import com.example.user_management.dto.CreateUserRequest;
import com.example.user_management.dto.LoginRequest;
import com.example.user_management.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.AccessTokenResponse;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService kcAdminClient;

    private final KeycloakProvider kcProvider;

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(UserController.class);


    public UserController(UserService kcAdminClient, KeycloakProvider kcProvider) {
        this.kcProvider = kcProvider;
        this.kcAdminClient = kcAdminClient;
    }


//    @PostMapping(value = "/create")
//    public String createUser(@RequestBody CreateUserRequest user) {
////        Response createdResponse = kcAdminClient.createKeycloakUser(user);
////        ResponseEntity test= ResponseEntity.status(createdResponse.getStatus()).build();
//        return "hello";
//    }
    @PostMapping(value = "/create")
    public ResponseEntity<AccessTokenResponse> createUser(@RequestBody CreateUserRequest user) {
        Response createdResponse = kcAdminClient.createKeycloakUser(user);

        if (createdResponse.getStatus() == 201) {
            Keycloak keycloak = kcProvider.newKeycloakBuilderWithPasswordCredentials(user.getEmail(),
                    user.getPassword()).build();

            try {
                AccessTokenResponse accessTokenResponse = keycloak.tokenManager().getAccessToken();
                return ResponseEntity.status(HttpStatus.CREATED).body(accessTokenResponse);
            } catch (BadRequestException ex) {
                LOG.warn("Failed to generate access token for new user.", ex);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        } else if (createdResponse.getStatus() == 409) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AccessTokenResponse> login(@NotNull @RequestBody LoginRequest loginRequest) {
        Keycloak keycloak = kcProvider.newKeycloakBuilderWithPasswordCredentials(loginRequest.getUsername(),
                loginRequest.getPassword()).build();

        AccessTokenResponse accessTokenResponse = null;
        try {
            accessTokenResponse = keycloak.tokenManager().getAccessToken();
            return ResponseEntity.status(HttpStatus.OK).body(accessTokenResponse);
        } catch (BadRequestException ex) {
            LOG.warn("invalid account. User probably hasn't verified email.", ex);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(accessTokenResponse);
        }

    }



}