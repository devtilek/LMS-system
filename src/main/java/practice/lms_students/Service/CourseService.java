package practice.lms_students.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import practice.lms_students.DTO.CourseDTO;
import practice.lms_students.Entity.Course;
import practice.lms_students.Entity.Role;
import practice.lms_students.Entity.User;
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

    public CourseDTO createCourse(CourseDTO courseDTO){
        User teacher = userRepo.findById(courseDTO.getTeacherId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (teacher.getRole() != Role.ROLE_TEACHER){
            throw new RuntimeException("Only Teacher can create course");
        }

        Course course = courseMapper.toEntity(courseDTO);
        course.setTeacher(teacher);

        courseRepo.save(course);

        return courseMapper.toDTO(course);
    }

    public List<CourseDTO> getAllCourses(){
        return  courseRepo.findAll()
                .stream()
                .map(courseMapper ::toDTO)
                .toList();
    }

    public CourseDTO getCourseById(Long id){
        Course course = courseRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        return courseMapper.toDTO(course);
    }

    public void delete(Long id){
        courseRepo.deleteById(id);
    }
}
