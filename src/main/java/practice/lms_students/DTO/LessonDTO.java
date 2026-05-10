package practice.lms_students.DTO;

import lombok.Data;
import practice.lms_students.Entity.Course;
@Data
public class LessonDTO {
    private Long id;
    private String title;
    private String content;
    private Long courseId;
}
