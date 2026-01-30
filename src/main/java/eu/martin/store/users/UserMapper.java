package eu.martin.store.users;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(constant = "USER", target = "role")
    @Mapping(ignore = true, target = "verified")
    @Mapping(ignore = true, target = "addresses")
    User toUserEntity(UserRequest dto);

    UserResponse toUserResponse(User user);

    @Mapping(ignore = true, target = "verified")
    @Mapping(ignore = true, target = "addresses")
    @Mapping(ignore = true, target = "role")
    void update(UserRequest dto, @MappingTarget User user);

    @Mapping(ignore = true, target = "loyaltyPoints")
    @Mapping(ignore = true, target = "user")
    void update(ProfileRequest dto, @MappingTarget Profile profile);

    ProfileResponse toProfileResponse(Profile profile);
}
