package tech.finaya.wallet.adapter.inbounds.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;

import tech.finaya.wallet.adapter.inbounds.controllers.api.UserAPI;
import tech.finaya.wallet.adapter.inbounds.dto.requests.CreateUserRequest;
import tech.finaya.wallet.adapter.inbounds.dto.responses.CreateUserResponse;
import tech.finaya.wallet.domain.models.User;
import tech.finaya.wallet.domain.usecases.CreateUser;
import tech.finaya.wallet.infrastructure.mappers.CreateUserMapper;

@RestController
@RequestMapping("/api/users")
public class UserController implements UserAPI {
    
    @Autowired
    private Logger log;

    @Autowired
    public CreateUser createUser;

    @PostMapping
    public ResponseEntity<CreateUserResponse> create(@RequestBody CreateUserRequest request) {
        log.info("Creating a user with name [{}] and CPF [{}]...", request.name(), request.cpf());

        User user = createUser.execute(CreateUserMapper.toEntity(request));

        log.info("User created with name [{}] and CPF [{}] and wallet [{}]...", user.getName(), user.getCpf(), user.getWallet().getWalletId());

        return ResponseEntity.ok(CreateUserMapper.toResponse(user));
    }

}
