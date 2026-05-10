package practice.lms_students.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import practice.lms_students.DTO.EnrollmentDTO;
import practice.lms_students.Entity.Course;
import practice.lms_students.Entity.Enrollment;
import practice.lms_students.Entity.Role;
import practice.lms_students.Entity.User;
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

    public EnrollmentDTO enroll(Long studentId, Long courseId){
        User student = userRepo.findById(studentId).orElseThrow(() -> new RuntimeException("User not found"));

        if (student.getRole() != Role.ROLE_STUDENT){
            throw new RuntimeException("Only Student can enroll");
        }

        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);

        enrollmentRepo.save(enrollment);

        return enrollmentMapper.toDTO(enrollment);
    }

    public List<EnrollmentDTO> getByStudent(Long studentId){
        return enrollmentRepo.findAll()
                .stream()
                .filter(s -> s.getStudent().getId().equals(studentId))
                .map(enrollmentMapper ::toDTO)
                .toList();
    }

    public List<EnrollmentDTO> getByCourse(Long courseId){
        return enrollmentRepo.findAll()
                .stream()
                .filter(e -> e.getCourse().getId().equals(courseId))
                .map(enrollmentMapper ::toDTO)
                .toList();
    }
}
