package eu.martin.store.users;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
interface UserMapper {
    User toEntity(UserRequest dto);

    UserResponse toResponse(User user);

    void update(UserRequest dto, @MappingTarget User user);
}
