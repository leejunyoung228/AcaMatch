package com.green.acamatch.reports;

import com.green.acamatch.entity.reports.Reports;
import com.green.acamatch.reports.model.PostReportsReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportsService {
    private final ReportsMapper reportsMapper;
    private final ReportsRepository reportsRepository;

    public int postReports(PostReportsReq req){
        Reports reports = new Reports();
        reports.setReporter(req.getReporter());
        if(req.getReportedUser() != null){
            reports.setReportedUser(req.getReportedUser());
        }
        if(req.getAcaId() != null){
            reports.setAcademy(req.getAcaId());
        }
        reports.setProcessingStatus(0);
        reports.setReportsType(req.getReportsType());
        reportsRepository.save(reports);
        return 1;
    }
}
