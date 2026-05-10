package practice.lms_students.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import practice.lms_students.Entity.Course;

@Repository
@Transactional
public interface CourseRepo extends JpaRepository<Course, Long> {
}
