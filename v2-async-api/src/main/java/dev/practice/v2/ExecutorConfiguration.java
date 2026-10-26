package dev.practice.v2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.*;
@Configuration
public class ExecutorConfiguration {
    @Bean(destroyMethod = "shutdown")
    public ExecutorService notificationExecutor() {
        return new ThreadPoolExecutor(4, 4, 0, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(100), new ThreadPoolExecutor.AbortPolicy());
    }
}