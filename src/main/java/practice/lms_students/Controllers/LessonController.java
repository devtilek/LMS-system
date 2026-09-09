package practice.lms_students.Controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
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
    @ResponseStatus(HttpStatus.CREATED)
    public LessonDTO createLesson(Authentication authentication,
                                  @Valid @RequestBody LessonDTO lessonDTO) {
        return lessonService.createLesson(authentication, lessonDTO);
    }

    @GetMapping("/course/{courseId}")
    public List<LessonDTO> getByCourse(@PathVariable Long courseId) {
        return lessonService.getByCourse(courseId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(Authentication authentication, @PathVariable Long id) {
        lessonService.deleteLesson(authentication, id);
    }
}
