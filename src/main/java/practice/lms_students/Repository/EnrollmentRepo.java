package practice.lms_students.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import practice.lms_students.Entity.Enrollment;

@Repository
@Transactional
public interface EnrollmentRepo extends JpaRepository<Enrollment, Long> {

}
