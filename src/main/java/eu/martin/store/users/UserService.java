package eu.martin.store.users;

import eu.martin.store.auth.AuthService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

import static eu.martin.store.common.Utils.*;

@Service
@AllArgsConstructor
class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final ProfileRepository profileRepository;
    private final AuthService authService;

    UserResponse registerUser(UserRequest dto) {
        if (userRepository.existsByEmail(dto.email()))
            throw new DuplicateUserException();

        var user = userMapper.toUserEntity(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        var registeredUser = userRepository.save(user);

        var profile = new Profile();
        profile.setUser(registeredUser);
        profileRepository.save(profile);

        return userMapper.toUserResponse(registeredUser);
    }

    UserResponse getUser(long id) {
        if (authorized(authService.getCurrentUser(), id))
            return userMapper.toUserResponse(findUserById(id));
        else
            throw new AccessDeniedException(FAILED_AUTHORIZATION);
    }

    private User findUserById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
    }

    Iterable<UserResponse> getAllUsers(String sortBy) {
        if (!Set.of("name", "email").contains(sortBy))
            sortBy = "name";

        return userRepository.findAll(Sort.by(sortBy))
                .stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    UserResponse updateUser(int id, UserRequest dto) {
        if (!authorized(authService.getCurrentUser(), id))
            throw new AccessDeniedException(FAILED_AUTHORIZATION);

        var user = findUserById(id);
        userMapper.update(dto, user);
        userRepository.save(user);

        return userMapper.toUserResponse(user);
    }

    private boolean authorized(User currentUser, long userId) {
        return currentUser.getRole().equals(Role.ADMIN) || userId == currentUser.getId();
    }

    void deleteUser(long id) {
        if (!userRepository.existsById(id))
            throw new EntityNotFoundException(USER_NOT_FOUND);

        if (authorized(authService.getCurrentUser(), id)) {
            profileRepository.deleteById(id);
            userRepository.deleteById(id);
        } else
            throw new AccessDeniedException(FAILED_AUTHORIZATION);
    }

    void changePassword(long id, ChangePasswordRequest dto) {
        if (!authorized(authService.getCurrentUser(), id))
            throw new AccessDeniedException(FAILED_AUTHORIZATION);

        var user = findUserById(id);

        if (!passwordEncoder.matches(dto.oldPassword(), user.getPassword()))
            throw new AccessDeniedException("Password does not match!");

        user.setPassword(dto.newPassword());
        userRepository.save(user);
    }

    ProfileResponse updateProfile(long userId, ProfileRequest dto) {
        if (!authorized(authService.getCurrentUser(), userId))
            throw new AccessDeniedException(FAILED_AUTHORIZATION);

        var profile = findProfile(userId);
        userMapper.update(dto, profile);

        return userMapper.toProfileResponse(profileRepository.save(profile));
    }

    ProfileResponse getProfile(long userId) {
        if (!authorized(authService.getCurrentUser(), userId))
            throw new AccessDeniedException(FAILED_AUTHORIZATION);

        var profile = findProfile(userId);
        return userMapper.toProfileResponse(profile);
    }

    private Profile findProfile(long id) {
        return profileRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(PROFILE_NOT_FOUND));
    }
}
