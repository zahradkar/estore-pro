package eu.martin.store.users;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@AllArgsConstructor
class UserService {
    private UserRepository userRepository;
    private UserMapper userMapper;

    UserResponse registerUser(UserRequest dto) {
        // todo test
        var registeredUser = userRepository.save(userMapper.toEntity(dto));
        return userMapper.toResponse(registeredUser);
    }

    UserResponse getUserById(int id) {
        return userMapper.toResponse(findUserById(id));
    }

    private User findUserById(int id) {
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

    @Transactional
    UserResponse updateUser(int id, UserRequest dto) {
        var user = findUserById(id);
        userMapper.update(dto, user);
        userRepository.save(user);

        return userMapper.toResponse(user);
    }

    void deleteUser(int id) {
        if (!userRepository.existsById(id))
            throw new EntityNotFoundException("User id not found!");
        userRepository.deleteById(id);
    }
}
