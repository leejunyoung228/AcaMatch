package com.green.acamatch.reports;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportsService {
    private final ReportsRepository reportsRepository;
}
