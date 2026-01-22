package eu.martin.store.users;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    UserResponse registerUser(UserRequest dto) { // todo test
        var user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        var registeredUser = userRepository.save(user);
        return userMapper.toResponse(registeredUser);
    }

    UserResponse getUser(long id) {
        return userMapper.toResponse(findUserById(id));
    }

    private User findUserById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User id not found!"));
    }

    Iterable<UserResponse> getAllUsers(String sortBy) {
        if (!Set.of("name", "email").contains(sortBy))
            sortBy = "name";

        return userRepository.findAll(Sort.by(sortBy))
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    UserResponse updateUser(int id, UserRequest dto) {
        var user = findUserById(id);
        userMapper.update(dto, user);
        userRepository.save(user);

        return userMapper.toResponse(user);
    }

    void deleteUser(long id) {
        if (!userRepository.existsById(id))
            throw new EntityNotFoundException("User id not found!");
        userRepository.deleteById(id);
    }

    void changePassword(long id, ChangePasswordRequest dto) {
        var user = findUserById(id);

        if (!passwordEncoder.matches(dto.oldPassword(), user.getPassword()))
            throw new AccessDeniedException("Password does not match!");

        user.setPassword(dto.newPassword());
        userRepository.save(user);
    }
}
