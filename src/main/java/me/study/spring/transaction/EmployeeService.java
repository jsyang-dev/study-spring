package me.study.spring.transaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final TeamService teamService;

    @Transactional
    public void saveEmployee() {
        log.info("saveEmployee: {}", TransactionSynchronizationManager.getCurrentTransactionName());

        Employee employee = new Employee();
        employee.setName("홍길동");
        employee.setCreateDate(LocalDateTime.now());
        employeeRepository.save(employee);

        try {
            teamService.saveTeam();
        } catch (RuntimeException e) {
            log.error("fail");
        }
    }
}
