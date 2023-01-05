package com.example.springbatchscheduling.mapper;

import com.example.springbatchscheduling.entity.User;
import com.example.springbatchscheduling.repository.UserRepository;
import com.opencsv.CSVWriter;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CSVFileMapper {

    private final UserRepository userRepository;

    public CSVFileMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<String[]> createCsvData() {
        List<User> list = userRepository.findAll();
        List<String[]> dataLines = new ArrayList<>();
        list.forEach(user -> {
            String[] data = {user.getId() + "", user.getUsername(), user.getEmail(), user.getBirthday().toString()};
            dataLines.add(data);
        });
        return dataLines;
    }

    public void saveCsvFile(String filename) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filename))) {
            writer.writeAll(createCsvData());
//            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
