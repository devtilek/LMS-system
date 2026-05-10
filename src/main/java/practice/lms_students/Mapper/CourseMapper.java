package practice.lms_students.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import practice.lms_students.DTO.CourseDTO;
import practice.lms_students.Entity.Course;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    @Mapping(source = "teacher.id",target = "teacherId")
    @Mapping(source = "teacher.fullName",target = "teacherName")
    CourseDTO toDTO(Course course);

    Course toEntity(CourseDTO courseDTO);
}
