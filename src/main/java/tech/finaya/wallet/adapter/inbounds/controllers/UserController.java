package tech.finaya.wallet.adapter.inbounds.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.finaya.wallet.adapter.inbounds.dto.requests.CreateUserRequest;
import tech.finaya.wallet.adapter.inbounds.dto.responses.CreateUserResponse;
import tech.finaya.wallet.domain.models.User;
import tech.finaya.wallet.domain.usecases.CreateUser;
import tech.finaya.wallet.infrastructure.mappers.CreateUserMapper;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    public CreateUser createUser;

    @PostMapping
    public ResponseEntity<CreateUserResponse> create(@RequestBody CreateUserRequest request) {
        User user = createUser.execute(CreateUserMapper.toEntity(request));

        return ResponseEntity.ok(CreateUserMapper.toResponse(user));
    }

}
