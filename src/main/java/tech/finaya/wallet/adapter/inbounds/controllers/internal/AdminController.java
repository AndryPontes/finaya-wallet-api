package tech.finaya.wallet.adapter.inbounds.controllers.internal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import tech.finaya.wallet.adapter.inbounds.controllers.api.internal.AdminAPI;
import tech.finaya.wallet.adapter.inbounds.dto.responses.UserResponse;
import tech.finaya.wallet.domain.usecases.FindAllUsers;
import tech.finaya.wallet.infrastructure.mappers.FindAllUsersMapper;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/internal")
public class AdminController implements AdminAPI {

    @Autowired
    public FindAllUsers findAllUsers;

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> findAllUsers() {
        return ResponseEntity.ok(FindAllUsersMapper.toListResponse(findAllUsers.execute()));
    }

}
