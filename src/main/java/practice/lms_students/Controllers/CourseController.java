package practice.lms_students.Controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import practice.lms_students.DTO.CourseDTO;
import practice.lms_students.Service.CourseService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CourseDTO createCourse(Authentication authentication,
                                  @Valid @RequestBody CourseDTO courseDTO) {
        return courseService.createCourse(authentication, courseDTO);
    }

    @GetMapping
    public List<CourseDTO> getAll() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{id}")
    public CourseDTO getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCourseById(Authentication authentication, @PathVariable Long id) {
        courseService.delete(authentication, id);
    }
}
