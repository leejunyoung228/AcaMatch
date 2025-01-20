package com.green.studybridge.joinClass;

import com.green.studybridge.joinClass.model.JoinClassDel;
import com.green.studybridge.joinClass.model.JoinClassPostReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinClassService {
    private final JoinClassMapper mapper;

    public int postJoinClass(JoinClassPostReq p) {
        return mapper.insJoinClass(p);
    }

    public int delJoinClass(JoinClassDel p) {
        return mapper.delJoinClass(p);
    }
}
