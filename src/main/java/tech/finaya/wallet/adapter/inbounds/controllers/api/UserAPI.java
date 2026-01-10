package tech.finaya.wallet.adapter.inbounds.controllers.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;

import tech.finaya.wallet.adapter.inbounds.dto.requests.CreateUserRequest;
import tech.finaya.wallet.adapter.inbounds.dto.responses.CreateUserResponse;

import org.springframework.http.ResponseEntity;

@Tag(name = "User", description = "Operations for users")
public interface UserAPI {

    @Operation(
        summary = "Create user",
        description = "Create a new user with name and CPF."
    )
    ResponseEntity<CreateUserResponse> create(@RequestBody CreateUserRequest request);

}
