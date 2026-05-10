package practice.lms_students.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import practice.lms_students.DTO.LessonDTO;
import practice.lms_students.Service.LessonService;

import java.util.List;

@RestController
@RequestMapping("/lessons")
@RequiredArgsConstructor
public class LessonController {
    private final LessonService lessonService;

    @PostMapping
    public LessonDTO createLesson(@RequestBody LessonDTO lessonDTO){
        return lessonService.createLesson(lessonDTO);
    }

    @GetMapping("/course/{courseId}")
    public List<LessonDTO> getById(@PathVariable Long courseId){
        return lessonService.getByCourse(courseId);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id){
        lessonService.deleteLesson(id);
    }
}
