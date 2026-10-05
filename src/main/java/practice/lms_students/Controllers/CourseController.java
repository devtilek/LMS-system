package practice.lms_students.Controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import practice.lms_students.DTO.CourseDTO;
import practice.lms_students.Service.CourseService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
@Validated
public class CourseController {
    private final CourseService courseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CourseDTO createCourse(Authentication authentication,
                                  @Valid @RequestBody CourseDTO courseDTO) {
        return courseService.createCourse(authentication, courseDTO);
    }

    @GetMapping
    public Page<CourseDTO> getAll(
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page must be greater than or equal to 0") int page,
            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Page size must be at least 1")
            @Max(value = 50, message = "Page size must be at most 50") int size) {
        return courseService.getAllCourses(page, size);
    }

    @GetMapping("/{id}")
    public CourseDTO getCourseById(@PathVariable @Positive(message = "Course ID must be positive") Long id) {
        return courseService.getCourseById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCourseById(Authentication authentication,
                                 @PathVariable @Positive(message = "Course ID must be positive") Long id) {
        courseService.delete(authentication, id);
    }
}
