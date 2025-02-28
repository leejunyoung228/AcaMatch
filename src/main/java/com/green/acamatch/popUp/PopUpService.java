package com.green.acamatch.popUp;

import com.green.acamatch.config.MyFileUtils;
import com.green.acamatch.config.exception.AcademyException;
import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.config.exception.UserMessage;
import com.green.acamatch.config.exception.popUpErrorCode;
import com.green.acamatch.entity.popUp.PopUp;
import com.green.acamatch.popUp.model.*;
import com.nimbusds.jose.util.IntegerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class PopUpService {
    private final PopUpMapper popUpMapper;
    private final PopUpRepository popUpRepository;
    private final MyFileUtils myFileUtils;
    private final UserMessage userMessage;

    @Transactional
    public int PostPopUp(MultipartFile pic, PopUpPostReq p) {
        PopUp popUp = new PopUp();
        popUp.setTitle(p.getTitle());
        popUp.setComment(p.getComment());
        popUp.setStartDate(p.getStartDate());
        popUp.setEndDate(p.getEndDate());
        popUp.setPopUpShow(p.getPopUpShow());
        popUp.setPopUpType(p.getPopUpType());

        popUpRepository.save(popUp);

        try {
            if (p.getComment() == null && pic == null) {
                throw new CustomException(popUpErrorCode.COMMENT_OR_PHOTO_REQUIRED); // 예외 처리

            } else if (p.getComment() != null && pic != null) {
                throw new CustomException(popUpErrorCode.COMMENT_OR_PHOTO_REQUIRED);

            } else if (pic != null) {

                long popUpId = popUp.getPopUpId();

                String middlePath = String.format("popUp/%d", popUpId);
                myFileUtils.makeFolders(middlePath);

                String savedPicName = pic != null ? myFileUtils.makeRandomFileName(pic) : null;
                String filePath = String.format("%s/%s", middlePath, savedPicName);

                try {
                    myFileUtils.transferTo(pic, filePath);
                    popUp.setPopUpPic(savedPicName);

                    popUpRepository.save(popUp);

                } catch (IOException e) {
                    String delFolderPath = String.format("%s/%s", myFileUtils.getUploadPath(), middlePath);
                    myFileUtils.deleteFolder(delFolderPath, true);
                    log.error("파일 저장 실패: " + e.getMessage());
                    throw new CustomException(AcademyException.PHOTO_SAVE_FAILED);
                }
            }
            return 1;
        } catch (CustomException e) {
            log.error("에러 발생: " + e.getMessage()); // 에러를 로깅;
            return 0;
        }
    }

    public List<PopUpGetDto> getPopUpList(PopUpGetReq p) {
        List<PopUpGetDto> result = popUpMapper.getPopUpList(p);
        return result;
    }

    public List<PopUpGetDetailRes> getPopUpDetail(PopUpGetDetailReq p) {
        List<PopUpGetDetailRes> result = popUpMapper.getPopUpDetail(p);
        return result;
    }

    @Transactional
    public int UpdPopUp(MultipartFile pic, PopUpPutReq p) {
        try {
            PopUp popUp = popUpRepository.findById(p.getPopUpId())
                    .orElseThrow(() -> new CustomException(popUpErrorCode.POPUP_NOT_FOUND));

            if (!StringUtils.hasText(p.getTitle())) {
                throw new CustomException(popUpErrorCode.FAIL_TO_UPD);
            }

            if (p.getPopUpShow() <= 0 || p.getPopUpType() <= 0) {
                throw new CustomException(popUpErrorCode.FAIL_TO_UPD);
            }

            boolean hasComment = p.getComment() != null && !p.getComment().isEmpty();
            boolean hasPic = pic != null && !pic.isEmpty();

            if (hasComment == hasPic) {
                throw new CustomException(popUpErrorCode.COMMENT_OR_PHOTO_REQUIRED);
            }

            popUp.setTitle(p.getTitle());
            popUp.setPopUpShow(p.getPopUpShow());
            popUp.setPopUpType(p.getPopUpType());

            if (hasComment) {
                popUp.setComment(p.getComment());
            } else {
                popUp.setComment(null);
            }

            if (hasPic) {
                // 파일 경로 설정
                long popUpId = popUp.getPopUpId();
                String middlePath = String.format("popUp/%d", popUpId);

                myFileUtils.deleteFolder(middlePath, true);

                myFileUtils.makeFolders(middlePath);

                // 새로운 파일 저장
                String savedPicName = myFileUtils.makeRandomFileName(pic);
                String filePath = String.format("%s/%s", middlePath, savedPicName);

                try {
                    myFileUtils.transferTo(pic, filePath);
                    popUp.setPopUpPic(savedPicName); // 파일 저장 후 popUp에 반영
                } catch (IOException e) {
                    myFileUtils.deleteFolder(middlePath, true);
                    throw new CustomException(popUpErrorCode.FAIL_TO_UPD);
                }
            } else {
                popUp.setPopUpPic(null);
            }
            popUpRepository.save(popUp);
            return 1;
        } catch (CustomException e) {
            e.getMessage();
            return 0;
        }
    }

    public int delPopUp(Long popUpId) {
        try {
            PopUp popUp = popUpRepository.findById(popUpId)
                    .orElseThrow(() -> new CustomException(popUpErrorCode.POPUP_NOT_FOUND));

            //팝업 사진 삭제 (폴더 삭제)
            String deletePath = String.format("popUp/%d", popUpId);
            myFileUtils.deleteFolder(deletePath, true);

            popUpRepository.delete(popUp);
            return 1;
        } catch (CustomException e) {
            e.getMessage();
            return 0;
        }
    }
}