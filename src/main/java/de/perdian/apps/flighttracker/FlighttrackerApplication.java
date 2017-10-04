package de.perdian.apps.flighttracker;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableConfigurationProperties
@EnableScheduling
public class FlighttrackerApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(FlighttrackerApplication.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
    }

    @Bean
    public TaskScheduler taskScheduler() {
        return new ThreadPoolTaskScheduler();
    }

}
