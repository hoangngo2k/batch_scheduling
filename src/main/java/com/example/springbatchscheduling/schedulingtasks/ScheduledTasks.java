package com.example.springbatchscheduling.schedulingtasks;

import com.example.springbatchscheduling.entity.User;
import com.example.springbatchscheduling.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class ScheduledTasks {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private final UserRepository repository;

    public ScheduledTasks(UserRepository repository) {
        this.repository = repository;
    }

    @Scheduled(fixedRate = (24*60*60*1000))
    public void reportBirthday() {
        List<User> list = repository.findAll();
        String dateOfBirth = sdf.format(new Date());
        list.forEach(user -> {
            if (dateOfBirth.compareTo(user.getBirthday().toString()) == 0) {
                log.info("Today is " + dateOfBirth + ". Happy birthday " + user.getUsername() + "!");
            }
        });
    }
}
