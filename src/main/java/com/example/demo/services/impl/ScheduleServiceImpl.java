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
    private final ScheduleMapper mapper = ScheduleMapper.INSTANCE;

    @Override
    public List<ScheduleEntryDto> getScheduleForGroup(Long groupId) {
        return scheduleEntryRepository.findByGroupSubjectTeacher_GroupId(groupId)
                .stream()
                .map(mapper::toScheduleEntryDto)
                .toList();
    }

    @Override
    public List<ScheduleEntryDto> getScheduleForTeacher(Long teacherId) {
        return scheduleEntryRepository.findByGroupSubjectTeacher_TeacherId(teacherId)
                .stream()
                .map(mapper::toScheduleEntryDto)
                .toList();
    }
}
