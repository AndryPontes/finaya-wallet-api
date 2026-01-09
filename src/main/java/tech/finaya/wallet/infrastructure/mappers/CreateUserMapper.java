package tech.finaya.wallet.infrastructure.mappers;

import tech.finaya.wallet.adapter.inbounds.dto.requests.CreateUserRequest;
import tech.finaya.wallet.adapter.inbounds.dto.responses.CreateUserResponse;
import tech.finaya.wallet.domain.models.User;

public class CreateUserMapper {
    
    public static User toEntity(CreateUserRequest request) {
        return new User(request.name());
    }

    public static CreateUserResponse toResponse(User user) {
        return new CreateUserResponse(user.getName(), user.isActive());
    }

}
