package me.study.spring.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tr")
@RequiredArgsConstructor
public class TrController {

    private final TrService trService;

    @PostMapping
    public String tr() {

        trService.saveEmployee();
        return "Success";
    }
}
