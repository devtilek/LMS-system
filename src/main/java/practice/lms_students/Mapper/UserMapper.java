package practice.lms_students.Mapper;

import org.mapstruct.Mapper;
import practice.lms_students.DTO.UserRequestDTO;
import practice.lms_students.DTO.UserResponseDTO;
import practice.lms_students.Entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDTO toDTO(User user);

    User toEntity(UserRequestDTO userDTO);
}
