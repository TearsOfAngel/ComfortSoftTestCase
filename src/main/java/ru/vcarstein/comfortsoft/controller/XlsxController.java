package ru.vcarstein.comfortsoft.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.vcarstein.comfortsoft.service.XlsxService;

@RestController
@RequestMapping("/api")
public class XlsxController {

    private final XlsxService xlsxService;

    public XlsxController(XlsxService xlsxService) {
        this.xlsxService = xlsxService;
    }

    @Operation(summary = "Получить N-ое минимальное число из Excel-файла")
    @GetMapping("/min")
    public int getNthSmallest(
            @RequestParam String filePath,
            @RequestParam int n
    ) throws Exception {
        return xlsxService.findNthSmallest(filePath, n);
    }
}
