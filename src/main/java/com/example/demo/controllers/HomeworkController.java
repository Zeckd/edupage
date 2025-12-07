package com.example.demo.controllers;

import com.example.demo.models.Homework;
import com.example.demo.services.HomeworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/homework")
@RequiredArgsConstructor
public class HomeworkController {

    private final HomeworkService homeworkService;

    @PostMapping("/set/{lessonId}")
    public Homework setHomework(@PathVariable Long lessonId, @RequestBody Homework homework) {
        return homeworkService.setHomework(lessonId, homework);
    }

    @GetMapping("/lesson/{lessonId}")
    public Homework getHomework(@PathVariable Long lessonId) {
        return homeworkService.getHomeworkByLesson(lessonId);
    }
}
