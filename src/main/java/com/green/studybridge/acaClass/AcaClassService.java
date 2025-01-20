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
public class AcaClassService {
    private final AcaClassMapper mapper;

        @Transactional
        public int postAcaClass(AcaClassPostReq p) {

            int exists = mapper.existsClass(p.getAcaId(),p.getClassName());
            if (exists > 0) {
                throw new IllegalArgumentException("이미 존재하는 수업입니다.");
            }

            int result = mapper.insAcaClass(p);
            return result;
        }

        public int insWeek(AcaClassDay p) {
            return mapper.insWeek(p);}

        public List<AcaClassDto> getClass(AcaClassGetReq p) {
            return mapper.selAcaClass(p);
        }

        public List<AcaClassToUserDto> getUserClass(AcaClassToUserGetReq p) {
            return mapper.selAcaClassToUser(p);
        }

        public List<AcaClassUserDto> getClassUser(AcaClassUserGetReq p) {
            return mapper.selAcaClassUser(p);
        }
        public int updAcaClass(AcaClassPutReq p) {
            return mapper.updAcaClass(p);
        }

        public int delAcaClass(AcaClassDelReq p) {
            return mapper.delAcaClass(p);
        }
}