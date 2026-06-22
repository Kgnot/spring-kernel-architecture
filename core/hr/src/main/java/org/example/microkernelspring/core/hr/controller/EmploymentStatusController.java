package org.example.microkernelspring.core.hr.controller;

import org.example.microkernelspring.core.hr.entity.LkpEmploymentStatus;
import org.example.microkernelspring.core.hr.service.EmploymentStatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hr/catalogs/employment-statuses")
public class EmploymentStatusController {

    private final EmploymentStatusService employmentStatusService;

    public EmploymentStatusController(
            EmploymentStatusService employmentStatusService
    ) {
        this.employmentStatusService = employmentStatusService;
    }

    @GetMapping
    public ResponseEntity<List<LkpEmploymentStatus>> findAll() {
        return ResponseEntity.ok(
                employmentStatusService.findAll()
        );
    }
}