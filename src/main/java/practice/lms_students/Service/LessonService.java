package practice.lms_students.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import practice.lms_students.DTO.LessonDTO;
import practice.lms_students.Entity.Course;
import practice.lms_students.Entity.Lesson;
import practice.lms_students.Mapper.LessonMapper;
import practice.lms_students.Repository.CourseRepo;
import practice.lms_students.Repository.LessonRepo;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonRepo lessonRepo;
    private final LessonMapper lessonMapper;
    private final CourseRepo courseRepo;

    public LessonDTO createLesson(LessonDTO lessonDTO){
        Course course = courseRepo.findById(lessonDTO.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Lesson lesson = lessonMapper.toEntity(lessonDTO);
        lesson.setCourse(course);

        lessonRepo.save(lesson);

        return lessonMapper.toDTO(lesson);
    }

    public List<LessonDTO> getByCourse(Long courseId){
        return lessonRepo.findAll()
                .stream()
                .filter(l -> l.getCourse().getId().equals(courseId))
                .map(lessonMapper ::toDTO)
                .toList();
    }

    public void deleteLesson(Long id){
        lessonRepo.deleteById(id);
    }

}
