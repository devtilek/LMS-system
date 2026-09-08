package practice.lms_students.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CourseDTO {
    private Long id;

    @NotBlank(message = "Course name is required")
    @Size(max = 150, message = "Course name must be at most 150 characters")
    private String name;

    @NotBlank(message = "Course description is required")
    @Size(max = 2000, message = "Course description must be at most 2000 characters")
    private String description;

    private Long teacherId;
    private String teacherName;
}
