package com.sprint.mission.sb8hrbankteamquerity.common.util;

import com.sprint.mission.sb8hrbankteamquerity.dto.department.DepartmentCreateRequest;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {

    public static List<DepartmentCreateRequest> toDepartmentRequests(InputStream is)
        throws IOException {
        List<DepartmentCreateRequest> list = new ArrayList<>(); // 엑셀의 부서 한 줄을 읽어서 부서 생성 요청 dto를 담을 list

        try (Workbook workbook = new XSSFWorkbook(is)) { // 엑셀 파일을 전체 읽어서 Workbook이라는 자바 객체로 변환
            Sheet sheet = workbook.getSheetAt(0); // 가장 첫번째 시트를 가져오기
            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // 엑셀 시트의 0줄은 부서 데이터의 헤더이므로 1 row부터 시작
                Row row = sheet.getRow(i); // 부서 목록 중 한 줄을 가져오기
                if (row == null) { // 만약, 엑셀 파일 중 부서 목록에 빈 줄이 있었다면
                    continue;
                }

                // 부서 목록 한 줄에서 부서명 부분
                Cell cellName = row.getCell(0); // 설명과 설립일만 적혀있고 부서명이 명시 안 된 경우 부서 생성 X
                if (cellName == null || cellName.getCellType() == CellType.BLANK) continue;
                String name = cellName.getStringCellValue();

                // 부서 목록 한 줄에서 부서 설명 부분
                Cell cellDesc = row.getCell(1);
                if (cellDesc == null || cellDesc.getCellType() == CellType.BLANK) continue;
                String description = cellDesc.getStringCellValue();

                // 부서 목록 한 줄에서 부서 설립일 부분
                Cell cellDate = row.getCell(2);
                if (cellDate == null || cellDate.getCellType() == CellType.BLANK) continue;
                // 엑셀 날짜 형식을 LocalDate로 변환
                LocalDate establishedDate = row.getCell(2).getLocalDateTimeCellValue()
                    .toLocalDate();

                // 부서 생성 정보를 dto list에 담아주기
                list.add(new DepartmentCreateRequest(name, description, establishedDate));
            }
        }
        return list;
    }
}
