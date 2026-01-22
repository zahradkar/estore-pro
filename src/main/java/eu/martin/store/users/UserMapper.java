package eu.martin.store.users;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRequest dto);

    UserResponse toResponse(User user); // todo check if necessary public (used in AuthController)

    void update(UserRequest dto, @MappingTarget User user);
}
