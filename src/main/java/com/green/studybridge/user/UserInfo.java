package com.green.studybridge.user;

import com.green.studybridge.user.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
public class UserInfo {
    MultipartFile pic;
    User user;
    public UserInfo(User user, MultipartFile pic) {
        this.user = user;
        this.pic = pic;
    }
}
