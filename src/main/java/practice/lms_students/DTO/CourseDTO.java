package practice.lms_students.DTO;

import lombok.Data;
@Data
public class CourseDTO {
    private Long id;
    private String name;
    private String description;
    private Long teacherId;
    private String teacherName;
}
