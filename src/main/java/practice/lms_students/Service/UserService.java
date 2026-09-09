package practice.lms_students.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import practice.lms_students.DTO.UserRequestDTO;
import practice.lms_students.DTO.UserResponseDTO;
import practice.lms_students.Entity.Role;
import practice.lms_students.Entity.User;
import practice.lms_students.Exception.AlreadyExistsException;
import practice.lms_students.Exception.ResourceNotFoundException;
import practice.lms_students.Mapper.UserMapper;
import practice.lms_students.Repository.UserRepo;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO register(UserRequestDTO request) {
        String email = request.getEmail().trim().toLowerCase();

        if (userRepo.findByEmail(email).isPresent()) {
            throw new AlreadyExistsException("Email is already registered");
        }

        User user = userMapper.toEntity(request);
        user.setEmail(email);
        user.setRole(Role.ROLE_STUDENT);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userMapper.toDTO(userRepo.save(user));
    }

    public UserResponseDTO getCurrentUser(Authentication authentication) {
        return userMapper.toDTO(getByEmail(authentication.getName()));
    }

    public User getByEmail(String email) {
        return userRepo.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
