package practice.lms_students.Service;

import lombok.RequiredArgsConstructor;
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

import java.util.List;

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

    public List<UserResponseDTO> getAll() {
        return userRepo.findAll().stream()
                .map(userMapper::toDTO)
                .toList();
    }

    public UserResponseDTO getById(Long id) {
        return userMapper.toDTO(findUser(id));
    }

    public void deleteById(Long id) {
        userRepo.delete(findUser(id));
    }

    public User getByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User findUser(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
