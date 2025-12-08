package com.example.demo.services.impl;

import com.example.demo.mappers.ScheduleMapper;
import com.example.demo.models.ScheduleEntry;
import com.example.demo.models.dtos.ScheduleEntryDto;
import com.example.demo.repositories.ScheduleEntryRepository;
import com.example.demo.services.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleEntryRepository scheduleEntryRepository;
    private final ScheduleMapper scheduleMapper;

    @Override
    public List<ScheduleEntryDto> getScheduleForGroup(Long groupId) {
        List<ScheduleEntry> entries = scheduleEntryRepository
                .findByGroupSubjectTeacher_GroupId(groupId);

        return entries.stream()
                .map(scheduleMapper::toScheduleEntryDto)
                .toList();
    }

    @Override
    public List<ScheduleEntryDto> getScheduleForTeacher(Long teacherId) {
        List<ScheduleEntry> entries = scheduleEntryRepository
                .findByGroupSubjectTeacher_TeacherId(teacherId);

        return entries.stream()
                .map(scheduleMapper::toScheduleEntryDto)
                .toList();
    }
}
