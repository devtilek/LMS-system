package practice.lms_students.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.lms_students.DTO.LessonDTO;
import practice.lms_students.Entity.Course;
import practice.lms_students.Entity.Lesson;
import practice.lms_students.Entity.Role;
import practice.lms_students.Entity.User;
import practice.lms_students.Exception.ResourceNotFoundException;
import practice.lms_students.Mapper.LessonMapper;
import practice.lms_students.Repository.CourseRepo;
import practice.lms_students.Repository.LessonRepo;
import practice.lms_students.Repository.UserRepo;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonRepo lessonRepo;
    private final LessonMapper lessonMapper;
    private final CourseRepo courseRepo;
    private final UserRepo userRepo;

    @Transactional
    public LessonDTO createLesson(Authentication authentication, LessonDTO lessonDTO) {
        User teacher = getAuthenticatedUser(authentication);
        requireTeacher(teacher);

        Course course = courseRepo.findById(lessonDTO.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        if (!course.getTeacher().getId().equals(teacher.getId())) {
            throw new AccessDeniedException("You can only add lessons to your own courses");
        }

        Lesson lesson = lessonMapper.toEntity(lessonDTO);
        lesson.setCourse(course);
        return lessonMapper.toDTO(lessonRepo.save(lesson));
    }

    @Transactional(readOnly = true)
    public List<LessonDTO> getByCourse(Long courseId) {
        if (!courseRepo.existsById(courseId)) {
            throw new ResourceNotFoundException("Course not found");
        }
        return lessonRepo.findByCourseId(courseId).stream()
                .map(lessonMapper::toDTO)
                .toList();
    }

    @Transactional
    public void deleteLesson(Authentication authentication, Long id) {
        User teacher = getAuthenticatedUser(authentication);
        requireTeacher(teacher);

        Lesson lesson = lessonRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));

        if (!lesson.getCourse().getTeacher().getId().equals(teacher.getId())) {
            throw new AccessDeniedException("You can only delete lessons from your own courses");
        }

        lessonRepo.delete(lesson);
    }

    private User getAuthenticatedUser(Authentication authentication) {
        return userRepo.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
    }

    private void requireTeacher(User user) {
        if (user.getRole() != Role.ROLE_TEACHER) {
            throw new AccessDeniedException("Only teachers can manage lessons");
        }
    }
}
