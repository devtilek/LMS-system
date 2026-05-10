package practice.lms_students.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import practice.lms_students.DTO.EnrollmentDTO;
import practice.lms_students.Service.EnrollmentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/enrollments")
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    @PostMapping
    public EnrollmentDTO enroll(@RequestParam Long studentId,
                                @RequestParam Long courseId){
        return enrollmentService.enroll(studentId, courseId);
    }

    @GetMapping("/student/{studentId}")
    public List<EnrollmentDTO> getByStudent(@PathVariable Long studentId){
        return enrollmentService.getByStudent(studentId);
    }

    @GetMapping("/course/{courseId}")
    public List<EnrollmentDTO> getByCourse(@PathVariable Long courseId){
        return enrollmentService.getByCourse(courseId);
    }
}
