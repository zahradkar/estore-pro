package eu.martin.store.users;

import eu.martin.store.auth.AuthService;
import eu.martin.store.auth.JwtService;
import eu.martin.store.email.EmailService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static eu.martin.store.common.Utils.*;

@Slf4j
@RequiredArgsConstructor
@Service
class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final ProfileRepository profileRepository;
    private final AuthService authService;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final Map<User, Date> recentlyRegisteredUsers = new HashMap<>();
    private boolean deletedOnStartup = false;

    @Value("${app.base-uri}")
    private String baseUrl;

    @Value("${app.user-path}")
    private String userPath;

    @Value("${spring.jwt.verificationExpiration}")
    private int verificationExpiration;

    @Transactional
    UserResponse registerUser(UserRequest dto) {
        if (userRepository.existsByEmail(dto.email()))
            throw new DuplicateUserException();

        var user = userMapper.toUserEntity(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        var registeredUser = userRepository.save(user);

        var profile = new Profile();
        profile.setUser(registeredUser);
        profileRepository.save(profile);

        generateAndSendVerificationToken(user);

        return userMapper.toUserResponse(registeredUser);
    }

    /**
     * Deletes users whose e-address was not verified within the verification period.
     */
    @Transactional
    @Scheduled(initialDelayString = "${spring.jwt.verificationExpiration}", timeUnit = TimeUnit.SECONDS, fixedDelayString = "${spring.jwt.verificationExpiration}")
    void deleteUnverifiedUsers() {
        if (!deletedOnStartup) {
            log.debug("Startup deletion of unverified users.");
            deletedOnStartup = true;

            var unverifiedAccounts = userRepository.getUnverifiedAccounts();

            for (var account : unverifiedAccounts)
                if (Duration.between(account.getRegisteredAt(), LocalDateTime.now()).getSeconds() >= verificationExpiration)
                    userRepository.deleteByEmail(account.getEmail());
        } else {
            log.debug("Regular deletion of unverified users.");
            // checks if given e-address is verified. If yes, removes it from the map, if no, checks if token of given e-address is expired. if yes, deletes given account. if no, skips (does nothing)

            recentlyRegisteredUsers.forEach((user, expirationDate) -> {
                if (user.isVerified())
                    recentlyRegisteredUsers.remove(user);
                else if (expirationDate.before(new Date()))
                    userRepository.deleteById(user.getId());
            });
        }
    }

    boolean verifyEmail(String token) {
        var jwt = jwtService.parseToken(token);
        if (jwt == null || !jwt.isVerification())
            return false;

        var user = userRepository.findById(jwt.getUserId()).orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
        user.setVerified(true);
        userRepository.save(user);
        return true;
    }

    String resendVerificationToken(String email) {
        var user = findByEmail(email);
        if (user.isVerified())
            throw new UserAlreadyVerifiedException();

        generateAndSendVerificationToken(user);
        return "new token sent";
    }

    private void generateAndSendVerificationToken(User user) {
        var jwt = jwtService.generateVerificationToken(user);
        var path = baseUrl + userPath + "/verify/" + jwt.toString();

        emailService.sendEmail( // verification e-mail
                user.getEmail(),
                "E-SHOP: e-mail address verification",
                """
                        %s, your account was successfully created.
                        To enable it, please, <a href=%s>confirm</a> your e-mail address."""
                        .formatted(user.getName(), path)
        );
        recentlyRegisteredUsers.put(user, jwt.claims().getExpiration());
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

    private User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
    }

    void resetPassword(String email) {
        var user = findByEmail(email);

        if (user.isVerified()) {
            emailService.sendEmail(email, "E-shop: password reset", ":-)");
        }
    }
}
