package me.study.spring.transaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrService {

    private final EmployeeRepository employeeRepository;
    private final TeamRepository teamRepository;

    @Transactional
    public void saveEmployee() {
        log.info("saveEmployee: {}", TransactionSynchronizationManager.getCurrentTransactionName());

        Employee employee = new Employee();
        employee.setName("홍길동");
        employee.setCreateDate(LocalDateTime.now());
        employeeRepository.save(employee);

        saveTeam();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveTeam() {
        log.info("saveTeam: {}", TransactionSynchronizationManager.getCurrentTransactionName());

        Team team = new Team();
        team.setName("영업팀");
        team.setCreateDate(LocalDateTime.now());
        teamRepository.save(team);

        throw new RuntimeException();
    }
}
