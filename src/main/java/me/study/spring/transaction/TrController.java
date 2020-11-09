package me.study.spring.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tr")
@RequiredArgsConstructor
public class TrController {

    private final EmployeeService employeeService;

    @PostMapping
    public String tr() {

        employeeService.saveEmployee();
        return "Success";
    }
}
