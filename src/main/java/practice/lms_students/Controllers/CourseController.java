package practice.lms_students.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import practice.lms_students.DTO.CourseDTO;
import practice.lms_students.Service.CourseService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;

    @PostMapping
    public CourseDTO createCourse(@RequestBody CourseDTO courseDTO){
        return courseService.createCourse(courseDTO);
    }

    @GetMapping()
    public List<CourseDTO> getAll(){
        return courseService.getAllCourses();
    }

    @GetMapping("/{id}")
    public CourseDTO getCourseById(@PathVariable Long id){
        return courseService.getCourseById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteCourseById(@PathVariable Long id){
        courseService.delete(id);
    }
}
