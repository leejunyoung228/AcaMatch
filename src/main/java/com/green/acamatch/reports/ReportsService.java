package com.green.acamatch.reports;

import com.green.acamatch.academy.AcademyRepository;
import com.green.acamatch.entity.academy.Academy;
import com.green.acamatch.entity.reports.Reports;
import com.green.acamatch.entity.reports.ReportsType;
import com.green.acamatch.entity.user.User;
import com.green.acamatch.reports.model.PostReportsReq;
import com.green.acamatch.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class ReportsService {
    private final ReportsMapper reportsMapper;
    private final ReportsRepository reportsRepository;
    private final UserRepository userRepository;
    private final AcademyRepository academyRepository;

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
        reports.setProcessingStatus(0);
        reports.setReportsType(req.getReportsType());
        reportsRepository.save(reports);
        return 1;
    }
}
