package practice.lms_students.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.lms_students.DTO.EnrollmentDTO;
import practice.lms_students.Entity.Course;
import practice.lms_students.Entity.Enrollment;
import practice.lms_students.Entity.User;
import practice.lms_students.Exception.AlreadyExistsException;
import practice.lms_students.Exception.ResourceNotFoundException;
import practice.lms_students.Mapper.EnrollmentMapper;
import practice.lms_students.Repository.CourseRepo;
import practice.lms_students.Repository.EnrollmentRepo;
import practice.lms_students.Repository.UserRepo;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {
    private final EnrollmentRepo enrollmentRepo;
    private final EnrollmentMapper enrollmentMapper;
    private final UserRepo userRepo;
    private final CourseRepo courseRepo;

    @Transactional
    public EnrollmentDTO enroll(Authentication authentication, Long courseId) {
        User student = getCurrentUser(authentication);
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        if (enrollmentRepo.existsByStudentIdAndCourseId(student.getId(), courseId)) {
            throw new AlreadyExistsException("Student is already enrolled in this course");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        return enrollmentMapper.toDTO(enrollmentRepo.save(enrollment));
    }

    public List<EnrollmentDTO> getByStudent(Authentication authentication, Long studentId) {
        User currentUser = getCurrentUser(authentication);
        if (!currentUser.getId().equals(studentId)) {
            throw new AccessDeniedException("You can only view your own enrollments");
        }

        return enrollmentRepo.findByStudentId(studentId).stream()
                .map(enrollmentMapper::toDTO)
                .toList();
    }

    public List<EnrollmentDTO> getByCourse(Long courseId) {
        if (!courseRepo.existsById(courseId)) {
            throw new ResourceNotFoundException("Course not found");
        }
        return enrollmentRepo.findByCourseId(courseId).stream()
                .map(enrollmentMapper::toDTO)
                .toList();
    }

    private User getCurrentUser(Authentication authentication) {
        return userRepo.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
    }
}
