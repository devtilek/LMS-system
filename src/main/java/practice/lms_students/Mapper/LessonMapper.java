package practice.lms_students.Mapper;

import jakarta.persistence.Id;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import practice.lms_students.DTO.LessonDTO;
import practice.lms_students.Entity.Lesson;

@Mapper(componentModel = "spring")
public interface LessonMapper {
    @Mapping(source = "course.id",target = "courseId")
    LessonDTO toDTO(Lesson lesson);

    Lesson toEntity(LessonDTO lessonDTO);
}
