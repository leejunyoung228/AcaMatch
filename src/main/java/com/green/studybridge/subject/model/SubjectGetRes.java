package com.green.studybridge.subject.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SubjectGetRes {
    List<SubjectGetDto> subjectGetDto;
}