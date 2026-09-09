package practice.lms_students.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
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
        User teacher = getAuthenticatedUser(authentication);

        if (teacher.getRole() != Role.ROLE_TEACHER) {
            throw new AccessDeniedException("Only teachers can create courses");
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

    @Transactional(readOnly = true)
    public CourseDTO getCourseById(Long id) {
        return courseMapper.toDTO(findCourse(id));
    }

    @Transactional
    public void delete(Authentication authentication, Long id) {
        User teacher = getAuthenticatedUser(authentication);
        if (teacher.getRole() != Role.ROLE_TEACHER) {
            throw new AccessDeniedException("Only teachers can delete courses");
        }

        Course course = findCourse(id);
        if (!course.getTeacher().getId().equals(teacher.getId())) {
            throw new AccessDeniedException("You can only delete your own courses");
        }

        courseRepo.delete(course);
    }

    private Course findCourse(Long id) {
        return courseRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
    }

    private User getAuthenticatedUser(Authentication authentication) {
        return userRepo.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
    }
}
