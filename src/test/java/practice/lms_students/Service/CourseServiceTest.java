package practice.lms_students.Service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import practice.lms_students.DTO.CourseDTO;
import practice.lms_students.Entity.Course;
import practice.lms_students.Entity.Role;
import practice.lms_students.Entity.User;
import practice.lms_students.Mapper.CourseMapper;
import practice.lms_students.Repository.CourseRepo;
import practice.lms_students.Repository.UserRepo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock CourseRepo courseRepo;
    @Mock CourseMapper courseMapper;
    @Mock UserRepo userRepo;
    @Mock Authentication authentication;

    @InjectMocks CourseService courseService;

    @Test
    void teacherCannotDeleteAnotherTeachersCourse() {
        User currentTeacher = user(1L, Role.ROLE_TEACHER);
        User owner = user(2L, Role.ROLE_TEACHER);
        Course course = new Course();
        course.setId(10L);
        course.setTeacher(owner);

        when(authentication.getName()).thenReturn("teacher@example.com");
        when(userRepo.findByEmail("teacher@example.com")).thenReturn(Optional.of(currentTeacher));
        when(courseRepo.findById(10L)).thenReturn(Optional.of(course));

        assertThrows(AccessDeniedException.class,
                () -> courseService.delete(authentication, 10L));

        verify(courseRepo, never()).delete(any(Course.class));
    }

    @Test
    void studentCannotCreateCourse() {
        User student = user(1L, Role.ROLE_STUDENT);
        when(authentication.getName()).thenReturn("student@example.com");
        when(userRepo.findByEmail("student@example.com")).thenReturn(Optional.of(student));

        assertThrows(AccessDeniedException.class,
                () -> courseService.createCourse(authentication, new CourseDTO()));

        verify(courseRepo, never()).save(any(Course.class));
    }

    private User user(Long id, Role role) {
        User user = new User();
        user.setId(id);
        user.setRole(role);
        return user;
    }
}
