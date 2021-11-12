package com.example.register.service.pay24;

import com.example.register.interfaces.Readable;
import com.example.register.model.Payment;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class Pay24RbsService implements Readable {
    @Override
    public List read(MultipartFile file) throws IOException {

        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet sheet = workbook.getSheetAt(0);
        List<Payment> list = new ArrayList<>();

        try {
            for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
                XSSFRow row = sheet.getRow(i);
                Payment rbs = new Payment();

                CellType sumType = row.getCell(3).getCellTypeEnum();

                switch (sumType) {
                    
                    case NUMERIC:
                        double money = row.getCell(3).getNumericCellValue();
                        rbs.setSum(money);
                        break;

                    case STRING:
                        String sum1 = row.getCell(3).getStringCellValue();
                        sum1 = sum1.replaceAll("\\u00a0", "");
                        sum1 = sum1.replaceAll(",", "");
                        if (!sum1.isEmpty()) {
                            double price = Double.parseDouble(sum1);
                            rbs.setSum(price);
                        } else {
                            log.error("Empty sum: {}", sum1);
                        }
                        break;
                    default:
                        log.error("Неизвестная ошибка: {}", row.getCell(2));
                }

                rbs.setDate(row.getCell(0).getStringCellValue());

                String[] operation = row.getCell(4).toString().split("Дата:");
                int size = operation.length;
                if (size > 1) {
                    String[] txn = operation[0].split("\\.");
                    String[] test = txn[txn.length - 2].split(" ");
                    String account = test[test.length - 1];
                    rbs.setAccount(account);
                } else {
                    rbs.setAccount(Arrays.toString(operation));
                    log.error("Номер реквизите или транзакции не обнаружена! {}", Arrays.toString(operation));
                }
                list.add(rbs);
            }
        } catch (NullPointerException | NumberFormatException | ArrayIndexOutOfBoundsException n) {
            n.printStackTrace();
        }

        return list;
    }
}
