package com.green.acamatch.reports;

import com.green.acamatch.academy.AcademyRepository;
import com.green.acamatch.entity.academy.Academy;
import com.green.acamatch.entity.reports.ActionType;
import com.green.acamatch.entity.reports.Reports;
import com.green.acamatch.entity.reports.ReportsType;
import com.green.acamatch.entity.review.Review;
import com.green.acamatch.entity.user.User;
import com.green.acamatch.reports.model.*;
import com.green.acamatch.review.ReviewRepository;
import com.green.acamatch.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportsService {
    private final ReportsMapper reportsMapper;
    private final ReportsRepository reportsRepository;
    private final UserRepository userRepository;
    private final AcademyRepository academyRepository;
    private final ReviewRepository reviewRepository;

    public int postReports(PostReportsReq req){
        User reporter = userRepository.findByUserId(req.getReporter()).orElse(null);
        Reports reports = new Reports();
        reports.setReporter(reporter);
        if(req.getReportedUser() != null){
            User reportedUser = userRepository.findByUserId(req.getReportedUser()).orElse(null);
            reports.setReportedUser(reportedUser);
        }
        if(req.getAcaId() != null){
            Academy academy = academyRepository.findById(req.getAcaId()).orElse(null);
            reports.setAcademy(academy);
        }
        if(req.getReviewId() != null){
            Review review = reviewRepository.findById(req.getReviewId()).orElse(null);
            reports.setReview(review);
        }
        reports.setProcessingStatus(0);
        reports.setReportsType(req.getReportsType());
        reportsRepository.save(reports);
        return 1;
    }

    public int updateReports(long reportsId, ActionType actionType){
        Reports reports = reportsRepository.findById(reportsId).orElse(null);
        reports.setProcessingStatus(1);
        if (actionType != ActionType.no_action) {
            reports.setExposureEndDate(reports.getUpdatedAt().plusDays(actionType.getDurationDays()));
        } else {
            reports.setExposureEndDate(null);
        }
        reports.setActionType(actionType);
        reportsRepository.save(reports);
        if(reports.getReview() != null){
            Review review = reviewRepository.findById(reports.getReview().getReviewId()).orElse(null);
            review.setBanReview(1);
            reviewRepository.save(review);
        }
        return 1;
    }

    public int deleteReports(long reportsId){
        Reports reports = reportsRepository.findById(reportsId).orElse(null);
        reportsRepository.delete(reports);
        return 1;
    }

    public List<GetUserListRes> getUserList(GetUserListReq req){
        List<GetUserListRes> userList = reportsMapper.getUserList(req);
        for(GetUserListRes userListRes : userList){
            if(userListRes.getActionType() == null){
                userListRes.setActionType(null);
            }
        }
        return userList;
    }

    public List<GetAcademyListRes> getAcademyList(GetAcademyListReq req){
        List<GetAcademyListRes> res = reportsMapper.getAcademyList(req);
        for(GetAcademyListRes re : res){
            if(re.getActionType() == null){
                re.setActionType(null);
            }
        }
        return res;
    }

    public List<GetReviewListRes> getReviewList(GetReviewListReq req){
        List<GetReviewListRes> res = reportsMapper.getReviewList(req);
        for(GetReviewListRes re : res){
            if(re.getActionType() == null){
                re.setActionType(null);
            }
        }
        return res;
    }
}
