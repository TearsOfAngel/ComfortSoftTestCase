package ru.vcarstein.comfortsoft.service.impl;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.vcarstein.comfortsoft.service.XlsxService;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class XlsxServiceImpl implements XlsxService {

    private static final Logger log = LoggerFactory.getLogger(XlsxServiceImpl.class);

    @Override
    public int findNthSmallest(String filePath, int n) throws Exception {
        List<Integer> numbers = readXlsxColumn(filePath);
        if (n < 1 || n > numbers.size()) {
            throw new IllegalArgumentException("Недопустимое значение N");
        }
        return quickSelect(numbers, 0, numbers.size() - 1, n - 1);
    }

    private List<Integer> readXlsxColumn(String filePath) throws Exception {
        List<Integer> result = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                Cell cell = row.getCell(0);
                if (cell != null && cell.getCellType() == CellType.NUMERIC) {
                    result.add((int) cell.getNumericCellValue());
                }
            }
        }
        return result;
    }

    private int quickSelect(List<Integer> list, int left, int right, int k) {
        log.debug("Вызван QuickSelect с диапазоном [{} - {}], ищем элемент с индексом {}", left, right, k);
        log.debug("Текущий фрагмент списка: {}", list.subList(left, right + 1));

        if (left == right) {
            log.debug("Остался один элемент: {}", list.get(left));
            return list.get(left);
        }

        int pivotIndex = partition(list, left, right);
        log.debug("Выбран опорный элемент на позиции {}, значение = {}", pivotIndex, list.get(pivotIndex));

        if (k == pivotIndex) {
            log.debug("Найден нужный элемент на позиции опорного: {}", list.get(k));
            return list.get(k);
        } else if (k < pivotIndex) {
            return quickSelect(list, left, pivotIndex - 1, k);
        } else {
            return quickSelect(list, pivotIndex + 1, right, k);
        }
    }

    private int partition(List<Integer> list, int left, int right) {
        int pivotIndex = left + new Random().nextInt(right - left + 1);
        int pivotValue = list.get(pivotIndex);
        swap(list, pivotIndex, right);
        int storeIndex = left;

        log.debug("Разделение по опорному значению {} на позиции {}", pivotValue, pivotIndex);

        for (int i = left; i < right; i++) {
            if (list.get(i) < pivotValue) {
                swap(list, storeIndex, i);
                storeIndex++;
            }
        }
        swap(list, storeIndex, right);

        log.debug("Список после разделения: {}", list.subList(left, right + 1));
        log.debug("Опорный элемент перемещён на позицию {}", storeIndex);

        return storeIndex;
    }

    private void swap(List<Integer> list, int i, int j) {
        int tmp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, tmp);
    }
}
