package com.green.studybridge.subject;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectMapper mapper;
}
