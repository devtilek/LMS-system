package practice.lms_students.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LessonDTO {
    private Long id;

    @NotBlank(message = "Lesson title is required")
    @Size(max = 200, message = "Lesson title must be at most 200 characters")
    private String title;

    @NotBlank(message = "Lesson content is required")
    private String content;

    @NotNull(message = "Course ID is required")
    private Long courseId;
}
