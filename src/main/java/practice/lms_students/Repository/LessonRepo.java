package practice.lms_students.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import practice.lms_students.Entity.Lesson;

import java.util.List;

@Repository
public interface LessonRepo extends JpaRepository<Lesson, Long> {
    List<Lesson> findByCourseId(Long courseId);
}
