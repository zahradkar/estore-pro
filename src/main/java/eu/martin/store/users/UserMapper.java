package eu.martin.store.users;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUserEntity(UserRequest dto);

    UserResponse toUserResponse(User user);

    void update(UserRequest dto, @MappingTarget User user);

    Profile toProfileEntity(ProfileRequest dto, long userId);

    ProfileResponse toProfileResponse(Profile profile);
}
