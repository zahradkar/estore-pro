package eu.martin.store.users;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(constant = "USER", target = "role")
    User toUserEntity(UserRequest dto);

    UserResponse toUserResponse(User user);

    void update(UserRequest dto, @MappingTarget User user);

    void update(ProfileRequest dto, @MappingTarget Profile profile);

    ProfileResponse toProfileResponse(Profile profile);
}
