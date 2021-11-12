package com.example.register.service.greenka;

import com.example.register.interfaces.Readable;
import com.example.register.model.Payment;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class GreenkaPartnerService implements Readable {

    @Override
    public List read(MultipartFile file) throws IOException {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet worksheet =  workbook.getSheetAt(0);

        List<Payment> list = new ArrayList<>();

        log.info("ПОПЫТКА ПАРСИНГА ПАРТНЕРСКОГО ФАЙЛА...");
        try{
            for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {

                Row row = worksheet.getRow(i);
                Payment partner = new Payment();
                String date = String.valueOf(row.getCell(5).getDateCellValue());
                String account = row.getCell(6).getStringCellValue();

                double sum = row.getCell(3).getNumericCellValue();

                partner.setDate(date);
                partner.setAccount(account);
                partner.setSum(sum);

                list.add(partner);
            }
        }catch (NullPointerException | IllegalStateException e){
            log.error("Пустое поле или строка в xlsx фале! {}", e);
        }
        return list;
    }
}
