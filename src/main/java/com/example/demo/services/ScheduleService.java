package com.example.demo.services;

import com.example.demo.models.dtos.ScheduleEntryDto;
import java.util.List;

public interface ScheduleService {

    List<ScheduleEntryDto> getScheduleForGroup(Long groupId);

    List<ScheduleEntryDto> getScheduleForTeacher(Long teacherId);
}
