package com.green.studybridge.acaClass;

import com.green.studybridge.acaClass.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class acaClassService {
    private final acaClassMapper mapper;

        @Transactional
        public int postAcaClass(acaClassPostReq p) {

            int exists = mapper.existsClass(p.getAcaId(),p.getClassName());
            if (exists > 0) {
                throw new IllegalArgumentException("이미 존재하는 수업입니다.");
            }

            int result = mapper.insAcaClass(p);
            return result;
        }

        public int insWeek(acaClassDay p) {
            return mapper.insWeek(p);}

        public List<acaClassDto> getClass(acaClassGetReq p) {
            return mapper.selAcaClass(p);
        }

        public List<acaClassUserDto> getUserClass(acaClassUserGetReq p) {
            return mapper.selAcaClassUser(p);
        }

        public int updAcaClass(acaClassPutReq p) {
            return mapper.updAcaClass(p);
        }

        public int delAcaClass(acaClassDelReq p) {
            return mapper.delAcaClass(p);
        }
}