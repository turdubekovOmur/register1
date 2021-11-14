package com.example.register.service.quickPay;

import com.example.register.interfaces.Readable;
import com.example.register.model.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class QuickPayPartnerService implements Readable {

    @Override
    public List read(MultipartFile file) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
        String line = "";
        List<Payment> list = new ArrayList<>();
        try {

            while ((line = in.readLine()) != null) {

                String row = line.replaceAll(";", " ");
                String[] records = row.split(" ");

                String account = records[0].replaceAll("\\u00a0", "");
                double sum = Double.parseDouble(records[4]);
                String date = records[1] + " " + records[2];

                Payment partner = new Payment();
                partner.setAccount(account);
                partner.setDate(date);
                partner.setSum(sum);

                list.add(partner);
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException | NullPointerException e) {
            log.error("Exception: {}", e.getMessage());
        }
        in.close();
        return list;
    }
}
