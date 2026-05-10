package practice.lms_students.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import practice.lms_students.DTO.EnrollmentDTO;
import practice.lms_students.Entity.Enrollment;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {
    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "course.id",target = "courseId")
    EnrollmentDTO toDTO(Enrollment enrollment);

    Enrollment toEntity(EnrollmentDTO enrollmentDTO);
}
