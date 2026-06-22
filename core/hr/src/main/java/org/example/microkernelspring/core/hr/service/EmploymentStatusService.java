package org.example.microkernelspring.core.hr.service;

import org.example.microkernelspring.core.hr.entity.LkpEmploymentStatus;
import org.example.microkernelspring.core.hr.repository.LkpEmploymentStatusRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class EmploymentStatusService {

    private final LkpEmploymentStatusRepository repository;

    public EmploymentStatusService(
            LkpEmploymentStatusRepository repository
    ) {
        this.repository = repository;
    }

    public List<LkpEmploymentStatus> findAll() {
        return repository.findAll();
    }
}