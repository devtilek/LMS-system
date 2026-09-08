package practice.lms_students.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.lms_students.DTO.CourseDTO;
import practice.lms_students.Entity.Course;
import practice.lms_students.Entity.Role;
import practice.lms_students.Entity.User;
import practice.lms_students.Exception.ResourceNotFoundException;
import practice.lms_students.Mapper.CourseMapper;
import practice.lms_students.Repository.CourseRepo;
import practice.lms_students.Repository.UserRepo;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepo courseRepo;
    private final CourseMapper courseMapper;
    private final UserRepo userRepo;

    @Transactional
    public CourseDTO createCourse(Authentication authentication, CourseDTO courseDTO) {
        User teacher = userRepo.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));

        if (teacher.getRole() != Role.ROLE_TEACHER) {
            throw new org.springframework.security.access.AccessDeniedException("Only teachers can create courses");
        }

        Course course = courseMapper.toEntity(courseDTO);
        course.setTeacher(teacher);
        return courseMapper.toDTO(courseRepo.save(course));
    }

    public List<CourseDTO> getAllCourses() {
        return courseRepo.findAll().stream()
                .map(courseMapper::toDTO)
                .toList();
    }

    public CourseDTO getCourseById(Long id) {
        Course course = courseRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        return courseMapper.toDTO(course);
    }

    public void delete(Long id) {
        if (!courseRepo.existsById(id)) {
            throw new ResourceNotFoundException("Course not found");
        }
        courseRepo.deleteById(id);
    }
}
