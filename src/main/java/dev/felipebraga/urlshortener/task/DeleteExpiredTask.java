package dev.felipebraga.urlshortener.task;

import dev.felipebraga.urlshortener.repository.UrlRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Component
public class DeleteExpiredTask {

    private final Log logger = LogFactory.getLog(getClass());

    private final UrlRepository urlRepository;

    public DeleteExpiredTask(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Scheduled(/*fixedDelay = 30, */timeUnit = TimeUnit.MINUTES, cron = Scheduled.CRON_DISABLED)
    public void deleteExpires() {
        logger.info("");
        urlRepository.deleteByExpiresInIsLessThan(LocalDateTime.now());
    }

}
