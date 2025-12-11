package com.example.demo.controllers;

import com.example.demo.models.dtos.HomeworkDto;
import com.example.demo.models.dtos.HomeworkRequestDto;
import com.example.demo.services.HomeworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/homework")
@RequiredArgsConstructor
public class HomeworkController {

    private final HomeworkService homeworkService;

    @PostMapping("/set/{lessonId}")
    public HomeworkDto setHomework(@PathVariable Long lessonId,
                                   @RequestBody HomeworkRequestDto dto) {
        return homeworkService.setHomework(lessonId, dto);
    }

    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<HomeworkDto> getHomework(@PathVariable Long lessonId) {
        HomeworkDto homework = homeworkService.getHomeworkByLesson(lessonId);
        if (homework == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(homework);
    }
}
