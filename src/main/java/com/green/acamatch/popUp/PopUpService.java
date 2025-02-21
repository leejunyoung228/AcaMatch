package com.green.acamatch.popUp;

import com.green.acamatch.config.MyFileUtils;
import com.green.acamatch.config.exception.AcademyException;
import com.green.acamatch.config.exception.CustomException;
import com.green.acamatch.entity.popUp.PopUp;
import com.green.acamatch.popUp.model.PopUpPostReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PopUpService {
    private final PopUpMapper popUpMapper;
    private final PopUpRepository popUpRepository;
    private final MyFileUtils myFileUtils;

    public int PostPopUp(MultipartFile pic, PopUpPostReq p) {
        PopUp popUp = new PopUp();
        popUp.setTitle(p.getTitle());
        popUp.setComment(p.getComment());
        popUp.setStartDate(p.getStartDate());
        popUp.setEndDate(p.getEndDate());
        popUp.setPopUpShow(p.getPopUpShow());
        popUp.setPopUpType(p.getPopUpType());

        popUpRepository.save(popUp);

        long popUpId = popUp.getPopUpId();

        String middlePath = String.format("popUp/%d", popUpId);
        myFileUtils.makeFolders(middlePath);

        String savedPicName = pic != null ? myFileUtils.makeRandomFileName(pic) : null;
        String filePath = String.format("%s/%s", middlePath, savedPicName);

        try {
            if (pic != null) {
                myFileUtils.transferTo(pic, filePath);
                popUp.setPopUpPic(filePath);
            }
            popUpRepository.save(popUp);
        } catch (IOException e) {
            String delFolderPath = String.format("%s/%s", myFileUtils.getUploadPath(), middlePath);
            myFileUtils.deleteFolder(delFolderPath, true);
            throw new CustomException(AcademyException.PHOTO_SAVE_FAILED);
        }
        return 1;
    }
}
