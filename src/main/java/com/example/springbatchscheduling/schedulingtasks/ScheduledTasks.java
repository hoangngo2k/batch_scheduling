package com.example.springbatchscheduling.schedulingtasks;

import com.example.springbatchscheduling.entity.User;
import com.example.springbatchscheduling.mapper.CSVFileMapper;
import com.example.springbatchscheduling.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class ScheduledTasks {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private final UserRepository repository;
    private final CSVFileMapper csvFileMapper;

    public ScheduledTasks(UserRepository repository, CSVFileMapper csvFileMapper) {
        this.repository = repository;
        this.csvFileMapper = csvFileMapper;
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
//        csvFileMapper.saveCsvFile("src/main/resources/users.csv");
    }
}
