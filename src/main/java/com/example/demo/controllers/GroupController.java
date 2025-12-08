package com.example.demo.controllers;

import com.example.demo.models.Group;
import com.example.demo.repositories.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupRepository groupRepository;

    @PostMapping
    public Group create(@RequestBody Group group) {
        return groupRepository.save(group);
    }

    @GetMapping
    public List<Group> all() {
        return groupRepository.findAll();
    }
}
