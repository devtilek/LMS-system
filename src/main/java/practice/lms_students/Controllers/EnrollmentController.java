package practice.lms_students.Controllers;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import practice.lms_students.DTO.EnrollmentDTO;
import practice.lms_students.Service.EnrollmentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/enrollments")
@Validated
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EnrollmentDTO enroll(Authentication authentication,
                                @RequestParam @Positive(message = "Course ID must be positive") Long courseId) {
        return enrollmentService.enroll(authentication, courseId);
    }

    @GetMapping("/student/{studentId}")
    public List<EnrollmentDTO> getByStudent(Authentication authentication,
                                            @PathVariable @Positive(message = "Student ID must be positive") Long studentId) {
        return enrollmentService.getByStudent(authentication, studentId);
    }

    @GetMapping("/course/{courseId}")
    public List<EnrollmentDTO> getByCourse(Authentication authentication,
                                           @PathVariable @Positive(message = "Course ID must be positive") Long courseId) {
        return enrollmentService.getByCourse(authentication, courseId);
    }
}
