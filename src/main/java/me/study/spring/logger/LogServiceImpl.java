package me.study.spring.logger;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    private final Logger logger;

    @Override
    public Boolean addLog(String str) {

        Boolean result = true;

        try {
            for (int i = 0; i < 10; i++) {
                result = logger.addQueue(str + i).get();
                if (!result) break;
            }
        } catch (Exception e) {
            return false;
        }

        return result;
    }
}
