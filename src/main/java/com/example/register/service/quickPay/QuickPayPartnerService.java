package com.example.register.service.quickPay;

import com.example.register.interfaces.Readable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class QuickPayPartnerService implements Readable {

    @Override
    public List read(MultipartFile file) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));

        Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
        for (CSVRecord record : records) {
            String name = String.valueOf(record);
            System.out.println(name);
        }
        return null;
    }
}
