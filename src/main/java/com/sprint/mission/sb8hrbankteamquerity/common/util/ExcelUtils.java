package com.sprint.mission.sb8hrbankteamquerity.common.util;

import com.sprint.mission.sb8hrbankteamquerity.dto.department.DepartmentCreateRequest;
import com.sprint.mission.sb8hrbankteamquerity.dto.department.DepartmentDto;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {

    public static List<DepartmentCreateRequest> toDepartmentRequests(InputStream is)
        throws IOException {
        List<DepartmentCreateRequest> list = new ArrayList<>(); // 엑셀의 부서 한 줄을 읽어서 부서 생성 요청 dto를 담을 list

        try (Workbook workbook = new XSSFWorkbook(is)) { // 엑셀 파일을 전체 읽어서 Workbook이라는 엑셀 객체로 변환
            Sheet sheet = workbook.getSheetAt(0); // 가장 첫번째 시트를 가져오기
            for (int i = 1; i <= sheet.getLastRowNum();
                i++) { // 엑셀 시트의 0줄은 부서 데이터의 헤더이므로 1 row부터 시작
                Row row = sheet.getRow(i); // 부서 목록 중 한 줄을 가져오기
                if (row == null) { // 만약, 엑셀 파일 중 부서 목록에 빈 줄이 있었다면
                    continue;
                }

                // 부서 목록 한 줄에서 부서명 부분
                Cell cellName = row.getCell(0); // 설명과 설립일만 적혀있고 부서명이 명시 안 된 경우 부서 생성 X
                if (cellName == null || cellName.getCellType() == CellType.BLANK) {
                    continue;
                }
                String name = cellName.getStringCellValue();

                // 부서 목록 한 줄에서 부서 설명 부분
                Cell cellDesc = row.getCell(1);
                if (cellDesc == null || cellDesc.getCellType() == CellType.BLANK) {
                    continue;
                }
                String description = cellDesc.getStringCellValue();

                // 부서 목록 한 줄에서 부서 설립일 부분
                Cell cellDate = row.getCell(2);
                if (cellDate == null || cellDate.getCellType() == CellType.BLANK) {
                    continue;
                }
                // 엑셀 날짜 형식을 LocalDate로 변환
                LocalDate establishedDate = row.getCell(2).getLocalDateTimeCellValue()
                    .toLocalDate();

                // 부서 생성 정보를 dto list에 담아주기
                list.add(new DepartmentCreateRequest(name, description, establishedDate));
            }
        }
        return list;
    }

    public static byte[] toDepartmentExcel(List<DepartmentDto> departments) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); // 메모리 상의 새로운 엑셀 파일 생성
            ByteArrayOutputStream os = new ByteArrayOutputStream()) { // 부서 목록 정보를 잠시 적어둘 저장소(바구니)

            Sheet sheet = workbook.createSheet("부서 목록"); // 엑셀 파일에서 시트 하나 생성
            String[] headers = {"부서명", "설명", "설립일"}; // 헤더 정보

            // 헤더 생성
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            // 엑셀에 부서 정보 채우기
            int rowIdx = 1; // 헤더를 제외하고 1번부터 시작
            for (DepartmentDto department : departments) {
                Row row = sheet.createRow(rowIdx++); // 부서 한 행 생성
                row.createCell(0).setCellValue(department.name()); // 부서명 채우기
                row.createCell(1).setCellValue(department.description()); // 설명 채우기
                row.createCell(2).setCellValue(department.establishedDate().toString()); // 설립일 채우기
            }

            workbook.write(os);
            return os.toByteArray();
        }
    }
}
