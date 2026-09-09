package practice.lms_students.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.lms_students.DTO.EnrollmentDTO;
import practice.lms_students.Entity.Course;
import practice.lms_students.Entity.Enrollment;
import practice.lms_students.Entity.Role;
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
        requireRole(student, Role.ROLE_STUDENT, "Only students can enroll in courses");

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

    @Transactional(readOnly = true)
    public List<EnrollmentDTO> getByStudent(Authentication authentication, Long studentId) {
        User currentUser = getCurrentUser(authentication);
        requireRole(currentUser, Role.ROLE_STUDENT, "Only students can view student enrollments");

        if (!currentUser.getId().equals(studentId)) {
            throw new AccessDeniedException("You can only view your own enrollments");
        }

        return enrollmentRepo.findByStudentId(studentId).stream()
                .map(enrollmentMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EnrollmentDTO> getByCourse(Authentication authentication, Long courseId) {
        User teacher = getCurrentUser(authentication);
        requireRole(teacher, Role.ROLE_TEACHER, "Only teachers can view course enrollments");

        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        if (!course.getTeacher().getId().equals(teacher.getId())) {
            throw new AccessDeniedException("You can only view enrollments for your own courses");
        }

        return enrollmentRepo.findByCourseId(courseId).stream()
                .map(enrollmentMapper::toDTO)
                .toList();
    }

    private User getCurrentUser(Authentication authentication) {
        return userRepo.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
    }

    private void requireRole(User user, Role role, String message) {
        if (user.getRole() != role) {
            throw new AccessDeniedException(message);
        }
    }
}
