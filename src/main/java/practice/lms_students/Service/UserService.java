package practice.lms_students.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import practice.lms_students.DTO.UserRequestDTO;
import practice.lms_students.DTO.UserResponseDTO;
import practice.lms_students.Entity.User;
import practice.lms_students.Mapper.UserMapper;
import practice.lms_students.Repository.UserRepo;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO register(UserRequestDTO userRequestDTO){
        userRepo.findByEmail(userRequestDTO.getEmail()).ifPresent(user -> {
            throw new RuntimeException("Email already exists");
        });

        User user = userMapper.toEntity(userRequestDTO);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);

        return userMapper.toDTO(user);
    }

    public List<UserResponseDTO> getAll(){
        return userRepo.findAll()
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

    public UserResponseDTO getById(Long id){
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return userMapper.toDTO(user);
    }

    public void deleteById(Long id){
        userRepo.deleteById(id);
    }

    public User getByEmail(String email){
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
