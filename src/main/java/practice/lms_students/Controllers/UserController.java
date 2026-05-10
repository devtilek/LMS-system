package practice.lms_students.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import practice.lms_students.DTO.UserRequestDTO;
import practice.lms_students.DTO.UserResponseDTO;
import practice.lms_students.Service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody UserRequestDTO userRequestDTO){
        return new ResponseEntity<>(userService.register(userRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping()
    public List<UserResponseDTO> getAll(){
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public UserResponseDTO getById(@PathVariable Long id){
        return userService.getById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id){
        userService.deleteById(id);
    }
}
