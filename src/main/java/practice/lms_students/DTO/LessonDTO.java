package practice.lms_students.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LessonDTO {
    private Long id;

    @NotBlank(message = "Lesson title is required")
    @Size(max = 200, message = "Lesson title must be at most 200 characters")
    private String title;

    @NotBlank(message = "Lesson content is required")
    @Size(max = 10000, message = "Lesson content must be at most 10000 characters")
    private String content;

    @NotNull(message = "Course ID is required")
    @Positive(message = "Course ID must be positive")
    private Long courseId;
}
