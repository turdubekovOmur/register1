package com.example.register.controller;

import com.example.register.model.Payment;
import com.example.register.service.greenka.GreenkaPartnerService;
import com.example.register.service.greenka.GreenkaRbsService;
import com.example.register.service.optima.OptimaPartnerService;
import com.example.register.service.optima.OptimaRbsService;
import com.example.register.service.pay24.Pay24PartnerService;
import com.example.register.service.pay24.Pay24RbsService;
import com.example.register.service.quickPay.QuickPayPartnerService;
import com.example.register.service.quickPay.QuickPayRbsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {

    private final OptimaRbsService optimaRbsService;
    private final OptimaPartnerService optimaPartnerService;
    private final Pay24RbsService pay24RbsService;
    private final Pay24PartnerService pay24PartnerService;
    private final GreenkaRbsService greenkaRbsService;
    private final GreenkaPartnerService greenkaPartnerService;
    private final QuickPayPartnerService quickPayPartnerService;
    private final QuickPayRbsService quickPayRbsService;


    private List<Payment> rbsList;
    private List<Payment> partnerList;

    private List<Payment> notMatchedRBS;
    private List<Payment> notMatchedPartner;

    @PostMapping("/import")
    public String mapReapExcel(@RequestParam("rbs_file") MultipartFile rbsFile, @RequestParam("partner_file") MultipartFile partnerFile, @RequestParam("partners") String partnerName) throws IOException {
        switch (partnerName) {
            case "optima":
                partnerList = optimaPartnerService.read(partnerFile);
                rbsList = optimaRbsService.read(rbsFile);
                break;
            case "pay24":
                rbsList = pay24RbsService.read(rbsFile);
                partnerList = pay24PartnerService.read(partnerFile);
                break;

            case "greenka":
                rbsList = greenkaRbsService.read(rbsFile);
                partnerList = greenkaPartnerService.read(partnerFile);
                break;
            case "quick pay":
                rbsList = quickPayRbsService.read(rbsFile);
                partnerList =  quickPayPartnerService.read(partnerFile);
                break;
        }

        log.info("Наименования партнера: {}", partnerName);
        log.info("Размер партнер листа: {}", partnerList.size());
        log.info("Размер рбс листа: {}", rbsList.size());
        log.info("Start to compare");
        compare();
        return "redirect:/list";
    }

    @GetMapping("/list")
    public String result(Model model) {
        model.addAttribute("notMatchedPartner", notMatchedPartner);
        model.addAttribute("notMatchedRBS", notMatchedRBS);
        return "result";
    }

    // TODO:СДЕЛАТЬ ОТДЕЛЬНЫЙ ПАРСИНГ КАЖДОГО СТОЛБЦА ДЛЯ OPTIMA!
    public void compare() {

        for (Payment partner : partnerList) {
            for (Payment rbs : rbsList) {
                if ( rbs.getSum() == partner.getSum() && rbs.getAccount().contains(partner.getAccount())) {
                    rbs.setExist(true);
                    partner.setExist(true);
                }
            }
        }
        notMatchedRBS = rbsList.stream().filter(rbs -> !rbs.isExist() && !(rbs.getAccount() == null || rbs.getSum() == 0)).collect(Collectors.toList());
        notMatchedPartner = partnerList.stream().filter(partner -> !partner.isExist() && !(partner.getAccount() == null || partner.getSum() == 0)).collect(Collectors.toList());
    }
}