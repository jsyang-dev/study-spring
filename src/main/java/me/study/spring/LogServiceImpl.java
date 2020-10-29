package me.study.spring;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    private final Logger logger;

    @Override
    public Boolean addLog(String str) {

        try {
            for (int i = 0; i < 100; i++) {
                logger.addQueue(str + i);
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
