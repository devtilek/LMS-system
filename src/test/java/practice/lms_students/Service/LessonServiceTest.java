package practice.lms_students.Service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import practice.lms_students.Entity.Course;
import practice.lms_students.Entity.Lesson;
import practice.lms_students.Entity.Role;
import practice.lms_students.Entity.User;
import practice.lms_students.Mapper.LessonMapper;
import practice.lms_students.Repository.CourseRepo;
import practice.lms_students.Repository.LessonRepo;
import practice.lms_students.Repository.UserRepo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonServiceTest {

    @Mock LessonRepo lessonRepo;
    @Mock LessonMapper lessonMapper;
    @Mock CourseRepo courseRepo;
    @Mock UserRepo userRepo;
    @Mock Authentication authentication;

    @InjectMocks LessonService lessonService;

    @Test
    void teacherCannotAddLessonToAnotherTeachersCourse() {
        User currentTeacher = user(1L, Role.ROLE_TEACHER);
        User owner = user(2L, Role.ROLE_TEACHER);
        Course course = new Course();
        course.setId(10L);
        course.setTeacher(owner);

        when(authentication.getName()).thenReturn("teacher@example.com");
        when(userRepo.findByEmail("teacher@example.com")).thenReturn(Optional.of(currentTeacher));
        when(courseRepo.findById(10L)).thenReturn(Optional.of(course));

        var dto = new practice.lms_students.DTO.LessonDTO();
        dto.setCourseId(10L);

        assertThrows(AccessDeniedException.class,
                () -> lessonService.createLesson(authentication, dto));

        verify(lessonRepo, never()).save(any(Lesson.class));
    }

    @Test
    void teacherCannotDeleteAnotherTeachersLesson() {
        User currentTeacher = user(1L, Role.ROLE_TEACHER);
        User owner = user(2L, Role.ROLE_TEACHER);
        Course course = new Course();
        course.setTeacher(owner);
        Lesson lesson = new Lesson();
        lesson.setId(20L);
        lesson.setCourse(course);

        when(authentication.getName()).thenReturn("teacher@example.com");
        when(userRepo.findByEmail("teacher@example.com")).thenReturn(Optional.of(currentTeacher));
        when(lessonRepo.findById(20L)).thenReturn(Optional.of(lesson));

        assertThrows(AccessDeniedException.class,
                () -> lessonService.deleteLesson(authentication, 20L));

        verify(lessonRepo, never()).delete(any(Lesson.class));
    }

    private User user(Long id, Role role) {
        User user = new User();
        user.setId(id);
        user.setRole(role);
        return user;
    }
}
