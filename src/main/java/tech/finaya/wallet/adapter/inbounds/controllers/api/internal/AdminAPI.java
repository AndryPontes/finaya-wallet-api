package tech.finaya.wallet.adapter.inbounds.controllers.api.internal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import tech.finaya.wallet.adapter.inbounds.dto.responses.UserResponse;

import java.util.List;

import org.springframework.http.ResponseEntity;

@Tag(name = "Admin", description = "Operations for admin")
public interface AdminAPI {

    @Operation(
        summary = "Find all users"
    )
    public ResponseEntity<List<UserResponse>> findAllUsers();

}
