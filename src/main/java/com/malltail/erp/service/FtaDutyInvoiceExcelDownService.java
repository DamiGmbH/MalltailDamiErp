package com.malltail.erp.service;

import com.malltail.erp.dto.FtaDutyInvoiceDto;
import com.malltail.erp.mapper.primary.FtaDutyInvoiceMapper;
import com.malltail.erp.mapper.second.ExcRateMapper;
import com.malltail.erp.vo.FtaDutyInvoiceVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FtaDutyInvoiceExcelDownService {

    @Value("${ftainvoice.image-name.excel-gmbh-img}")// gmbh 이미지 명
    String excel_gmbh_img;
    @Value("${ftainvoice.image-name.excel-sign-img}")// sign 이미지 명
    String excel_sign_img;

    //대상 데이터 Mapper
    private final FtaDutyInvoiceMapper ftaDutyInvoiceMapper;

    //환율정보 데이터 Mapper
    private final ExcRateMapper excRateMapper;

    /**
     * FTA면세인보이스 대상 데이터 조회
     * @param ftaDutyInvoiceDto
     * @return
     */
    public List<FtaDutyInvoiceVo> ftaDutyInvoiceList(FtaDutyInvoiceDto ftaDutyInvoiceDto){

        return  ftaDutyInvoiceMapper.ExcelDownList(ftaDutyInvoiceDto);

    }

    /**
     * FTA면세인보이스 엑셀파일 생성 처리
     * @param ftaDutyInvoiceVo
     * @return
     * @throws IOException
     */
    public Workbook createFtaDutyInvoiceExcelDown(List<FtaDutyInvoiceVo> ftaDutyInvoiceVo) throws IOException {

        LocalDate date = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Workbook workbook = new XSSFWorkbook();
        //USD 환율정보(다배송어드민 DB에서 가져온다)
        double rateUsd = excRateMapper.excRateUsd();

        String sFontNm = "맑은 고딕";

        // 달러표기 셀 설정을 위한 포맷
        DataFormat cellFormat = workbook.createDataFormat();

        // 첫라인 타이틀 세팅
        Font font0 = workbook.createFont();
        font0.setFontName(sFontNm); //폰트이름
        font0.setFontHeight((short)400); //폰트 size -> 400 = 20point
        font0.setBold(true); // Bold 설정
        font0.setUnderline(Font.U_SINGLE);

        CellStyle basicStyle0 = workbook.createCellStyle(); //style선언
        basicStyle0.setFont(font0); // 위에 선언한 font 적용
        basicStyle0.setAlignment(HorizontalAlignment.CENTER); // 가로 가운데 정렬
        basicStyle0.setVerticalAlignment(VerticalAlignment.CENTER); // 세로 가운데 정렬

        // 항목명1 세팅
        Font font = workbook.createFont();
        font.setFontName(sFontNm); //폰트이름
        font.setFontHeight((short)180); //폰트 size -> 180 = 9point
        font.setBold(true); // Bold 설정

        CellStyle titleStyle1 = workbook.createCellStyle(); //style선언
        titleStyle1.setFont(font); // 위에 선언한 font 적용
        titleStyle1.setVerticalAlignment(VerticalAlignment.CENTER); // 세로 가운데 정렬
        titleStyle1.setBorderTop(BorderStyle.THIN); // 셀 위 테두리 실선 적용
        titleStyle1.setBorderLeft(BorderStyle.THIN); // 셀 왼쪽 테두리 실선 적용

        // 항목명2 세팅
        Font font2 = workbook.createFont();
        font2.setFontName(sFontNm); //폰트이름
        font2.setFontHeight((short)180); //폰트 size -> 180 = 9point
        font2.setBold(true); // Bold 설정

        CellStyle titleStyle2 = workbook.createCellStyle(); //style선언
        titleStyle2.setFont(font2); // 위에 선언한 font 적용
        titleStyle2.setVerticalAlignment(VerticalAlignment.CENTER); // 세로 가운데 정렬
        titleStyle2.setAlignment(HorizontalAlignment.CENTER);
        titleStyle2.setBorderTop(BorderStyle.THIN); // 셀 위 테두리 실선 적용
        titleStyle2.setBorderLeft(BorderStyle.THIN); // 셀 왼쪽 테두리 실선 적용
        titleStyle2.setBorderRight(BorderStyle.THIN); // 셀 오른쪽 테두리 실선 적용

        //값세팅
        Font font3 = workbook.createFont();
        font3.setFontName(sFontNm); //폰트이름
        font3.setFontHeight((short)160); //폰트 size -> 160 = 8point

        Font font4 = workbook.createFont();
        font4.setFontName(sFontNm); //폰트이름
        font4.setFontHeight((short)220); //폰트 size -> 220 = 11point

        Font font5 = workbook.createFont();
        font5.setFontName(sFontNm); //폰트이름
        font5.setFontHeight((short)180); //폰트 size -> 180 = 9point

        CellStyle valueStyle0 = workbook.createCellStyle(); //style선언
        valueStyle0.setFont(font5); // 위에 선언한 font 적용

        CellStyle valueStyle1 = workbook.createCellStyle(); //style선언
        valueStyle1.setFont(font3); // 위에 선언한 font 적용
        valueStyle1.setBorderLeft(BorderStyle.THIN); // 셀 오른쪽 테두리 실선 적용
        valueStyle1.setIndention((short) 1); // 들여쓰기 수준 설정

        CellStyle valueStyle2 = workbook.createCellStyle(); //style선언
        valueStyle2.setFont(font4); // 위에 선언한 font 적용

        CellStyle valueStyle3 = workbook.createCellStyle(); //style선언
        valueStyle3.setFont(font3); // 위에 선언한 font 적용
        valueStyle3.setBorderRight(BorderStyle.THIN); // 셀 오른쪽 테두리 실선 적용
        valueStyle3.setAlignment(HorizontalAlignment.RIGHT);
        valueStyle3.setDataFormat(cellFormat.getFormat("$#,##0.0")); // 달러표기 서식 설정

        CellStyle valueStyle4 = workbook.createCellStyle(); //style선언
        valueStyle4.setFont(font3); // 위에 선언한 font 적용
        valueStyle4.setBorderLeft(BorderStyle.THIN); // 셀 왼쪽 테두리 실선 적용
        valueStyle4.setBorderRight(BorderStyle.THIN); // 셀 오른쪽 테두리 실선 적용
        valueStyle4.setAlignment(HorizontalAlignment.CENTER);

        CellStyle valueStyleTR = workbook.createCellStyle();
        valueStyleTR.setFont(font3); // 위에 선언한 font 적용
        valueStyleTR.setBorderTop(BorderStyle.THIN); // 셀 위 테두리 실선 적용
        valueStyleTR.setBorderRight(BorderStyle.THIN); // 셀 오른쪽 테두리 실선 적용
        valueStyleTR.setAlignment(HorizontalAlignment.CENTER);

        CellStyle valueStyleTL = workbook.createCellStyle();
        valueStyleTL.setFont(font2); // 위에 선언한 font 적용
        valueStyleTL.setBorderTop(BorderStyle.THIN); // 셀 위 테두리 실선 적용
        valueStyleTL.setBorderLeft(BorderStyle.THIN); // 셀 오른쪽 테두리 실선 적용

        CellStyle valueStyleRight = workbook.createCellStyle();
        valueStyleRight.setBorderRight(BorderStyle.THIN); // 셀 오른쪽 테두리 실선 적용

        CellStyle valueStyleLeft = workbook.createCellStyle();
        valueStyleLeft.setBorderLeft(BorderStyle.THIN); // 셀 왼쪽 테두리 실선 적용

        for (FtaDutyInvoiceVo ftaDutyList : ftaDutyInvoiceVo) {

            Sheet sheet = workbook.createSheet(ftaDutyList.getTrackingNum()+"-"+ftaDutyList.getReceiverName());

            double goodsPrice = ftaDutyList.getUnitPrice() * rateUsd;
            goodsPrice = Math.round(goodsPrice * 100d) / 100d;

            // 인쇄 페이지 설정
            PrintSetup printSetup = sheet.getPrintSetup();
            printSetup.setPaperSize(PrintSetup.A4_PAPERSIZE); //A4용지 설정
            printSetup.setLandscape(false); // 세로 모드
            printSetup.setFitHeight((short) 1);
            printSetup.setFitWidth((short) 1);

            // 모든 내용을 1페이지 안에 보이도록 설정
            sheet.setFitToPage(true);
            sheet.setAutobreaks(false);

            // 인쇄 여백 설정
            sheet.setMargin(Sheet.TopMargin, 0.5);
            sheet.setMargin(Sheet.BottomMargin, 0.5);
            sheet.setMargin(Sheet.LeftMargin, 1);
            sheet.setMargin(Sheet.RightMargin, 1);

            //셀별 크기설정.
            sheet.setColumnWidth(0,3000);
            sheet.setColumnWidth(1,11000);
            sheet.setColumnWidth(2,6000);
            sheet.setColumnWidth(3,8000);

            //첫번째 열 병합
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
            //3번째 열 병합
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 1));
            sheet.addMergedRegion(new CellRangeAddress(3, 3, 0, 1));
            sheet.addMergedRegion(new CellRangeAddress(4, 4, 0, 1));
            sheet.addMergedRegion(new CellRangeAddress(5, 5, 0, 1));
            //7번째 열 병합
            sheet.addMergedRegion(new CellRangeAddress(6, 6, 0, 1));
            sheet.addMergedRegion(new CellRangeAddress(7, 7, 0, 1));
            sheet.addMergedRegion(new CellRangeAddress(8, 8, 0, 1));
            sheet.addMergedRegion(new CellRangeAddress(9, 9, 0, 1));
            //26번째 열 병합
            sheet.addMergedRegion(new CellRangeAddress(25, 25, 0, 1));

            // A1 셀에 "COMMERCIAL INVOICE" 텍스트 설정
            Row rowA1 = sheet.createRow(0);
            Cell cellA1 = rowA1.createCell(0);
            cellA1.setCellValue("COMMERCIAL INVOICE");
            cellA1.setCellStyle(basicStyle0);

            // A3 셀에 "1. Shipper/Exporter" 텍스트 설정
            Row rowA3 = sheet.createRow(2);
            Cell cellA3 = rowA3.createCell(0);
            cellA3.setCellValue("1. Shipper/Exporter");
            cellA3.setCellStyle(titleStyle1);

            // A4 셀에 "Onpro GmbH+업체명" 텍스트 설정
            Row rowA4 = sheet.createRow(3);
            Cell cellA4 = rowA4.createCell(0);
            cellA4.setCellValue("Onpro GmbH ("+ftaDutyList.getCompanyName()+")");
            cellA4.setCellStyle(valueStyle1);

            // A5 셀에 "Am Planetarium 8, 07743 Jena Germany" 텍스트 설정
            Row rowA5 = sheet.createRow(4);
            Cell cellA5 = rowA5.createCell(0);
            cellA5.setCellValue("Am Planetarium 8, 07743 Jena Germany");
            cellA5.setCellStyle(valueStyle1);

            // A6 셀에 테두리 설정
            Row rowA6 = sheet.createRow(5);
            Cell cellA6 = rowA6.createCell(0);
            cellA6.setCellStyle(valueStyle1);

            // A7 셀에 "2. Consignee" 텍스트 설정
            Row rowA7 = sheet.createRow(6);
            Cell cellA7 = rowA7.createCell(0);
            cellA7.setCellValue("2. Consignee");
            cellA7.setCellStyle(titleStyle1);

            // A8 셀에 받는분+전화번호 설정
            Row rowA8 = sheet.createRow(7);
            Cell cellA8 = rowA8.createCell(0);
            cellA8.setCellValue(ftaDutyList.getReceiverName()+"("+ftaDutyList.getPhoneNum()+")");
            cellA8.setCellStyle(valueStyle1);

            // A9 셀에 주소 설정
            Row rowA9 = sheet.createRow(8);
            Cell cellA9 = rowA9.createCell(0);
            cellA9.setCellValue(ftaDutyList.getAddress());
            cellA9.setCellStyle(valueStyle1);

            // A10 셀에 "SOUTH KOREA" 텍스트 설정
            Row rowA10 = sheet.createRow(9);
            Cell cellA10 = rowA10.createCell(0);
            cellA10.setCellValue("SOUTH KOREA");
            cellA10.setCellStyle(valueStyle1);

            // A11 셀에 테두리 설정
            Row rowA11 = sheet.createRow(10);
            Cell cellA11 = rowA11.createCell(0);
            cellA11.setCellStyle(valueStyle1);

            // A12 셀에 "6. No of UNITS" 텍스트 설정
            Row rowA12 = sheet.createRow(11);
            Cell cellA12 = rowA12.createCell(0);
            cellA12.setCellValue("6. No of UNITS");
            cellA12.setCellStyle(titleStyle2);

            // A13 셀에 수량 설정
            Row rowA13 = sheet.createRow(12);
            Cell cellA13 = rowA13.createCell(0);
            cellA13.setCellValue(ftaDutyList.getItemCnt());
            cellA13.setCellStyle(valueStyle4);

            // A22 셀에 안내문구1 텍스트 설정
            Row rowA22 = sheet.createRow(21);
            Cell cellA22 = rowA22.createCell(0);
            cellA22.setCellValue("The exporter of the products covered by this document declares that, except where otherwise clearly indicted,");
            cellA22.setCellStyle(valueStyle0);

            // A30 셀에 안내문구2 텍스트 설정
            Row rowA23 = sheet.createRow(22);
            Cell cellA23 = rowA23.createCell(0);
            cellA23.setCellValue("these products are of EU preferential origin.");
            cellA23.setCellStyle(valueStyle0);

            // A25 셀에 안내문구3 텍스트 설정
            Row rowA25 = sheet.createRow(24);
            Cell cellA25 = rowA25.createCell(0);
            cellA25.setCellValue("Besides, the exporter declares that all information in this invoice is true and correct.");
            cellA25.setCellStyle(valueStyle0);

            //B3 셀에 테두리 설정
            Cell cellB3 = rowA3.createCell(1);
            cellB3.setCellStyle(valueStyleTR);

            // B7 셀에 테두리 설정
            Cell cellB7 = rowA7.createCell(1);
            cellB7.setCellStyle(valueStyleTR);

            // B12 셀에 "7. Description of goods" 텍스트 설정
            Cell cellB12 = rowA12.createCell(1);
            cellB12.setCellValue("7. Description of goods");
            cellB12.setCellStyle(titleStyle2);

            // B13 셀에 품목명 설정
            Cell cellB13 = rowA13.createCell(1);
            cellB13.setCellValue(ftaDutyList.getItemName());
            cellB13.setCellStyle(valueStyle4);

            //C3 셀에 "3. Date of invoice" 텍스트 설정
            Cell cellC3 = rowA3.createCell(2);
            cellC3.setCellValue("3. Date of invoice");
            cellC3.setCellStyle(titleStyle1);

            // C4 셀에 테두리 설정
            Cell cellC4 = rowA4.createCell(2);
            cellC4.setCellStyle(valueStyleLeft);

            // C5 셀에 "4. No of HAWB" 텍스트 설정
            Cell cellC5 = rowA5.createCell(2);
            cellC5.setCellValue("4. No of HAWB");
            cellC5.setCellStyle(valueStyleTL);

            // C6 셀에 테두리 설정
            Cell cellC6 = rowA6.createCell(2);
            cellC6.setCellStyle(valueStyleLeft);

            // C7 셀에 "5. Remark" 텍스트 설정
            Cell cellC7 = rowA7.createCell(2);
            cellC7.setCellValue("5. Remark");
            cellC7.setCellStyle(titleStyle1);

            // C8 셀에 테두리 설정
            Cell cellC8 = rowA8.createCell(2);
            cellC8.setCellStyle(valueStyleLeft);

            // C9 셀에 테두리 설정
            Cell cellC9 = rowA9.createCell(2);
            cellC9.setCellStyle(valueStyleLeft);

            // C10 셀에 테두리 설정
            Cell cellC10 = rowA10.createCell(2);
            cellC10.setCellStyle(valueStyleLeft);

            // C11 셀에 테두리 설정
            Cell cellC11 = rowA11.createCell(2);
            cellC11.setCellStyle(valueStyleLeft);

            // C12 셀에 "8. UNIT VALUE" 텍스트 설정
            Cell cellC12 = rowA12.createCell(2);
            cellC12.setCellValue("8. UNIT VALUE");
            cellC12.setCellStyle(titleStyle2);

            // C13 셀에 단가 설정
            Cell cellC13 = rowA13.createCell(2);
            //cellC13.setCellValue(ftaDutyList.getUnitPrice());
            cellC13.setCellValue(goodsPrice);
            cellC13.setCellStyle(valueStyle3);

            // C36 셀에 단가 설정
            Row rowC36 = sheet.createRow(28);
            Cell cellC36 = rowC36.createCell(2);
            cellC36.setCellValue("Position : Manager");
            cellC36.setCellStyle(valueStyle2);

            // C37 셀에 단가 설정
            Row rowC37 = sheet.createRow(29);
            Cell cellC37 = rowC37.createCell(2);
            cellC37.setCellValue("Name : Kyung Man Kwak");
            cellC37.setCellStyle(valueStyle2);

            // D3 셀에 현재날짜값 설정
            Cell cellD3 = rowA3.createCell(3);
            cellD3.setCellValue(date.format(formatter));
            cellD3.setCellStyle(valueStyleTR);

            // D4 셀에 테두리 설정
            Cell cellD4 = rowA4.createCell(3);
            cellD4.setCellStyle(valueStyleRight);

            // D5 셀에 송장번호 설정
            Cell cellD5 = rowA5.createCell(3);
            cellD5.setCellValue(ftaDutyList.getTrackingNum());
            cellD5.setCellStyle(valueStyleTR);

            // D6 셀에 테두리 설정
            Cell cellD6 = rowA6.createCell(3);
            cellD6.setCellStyle(valueStyleRight);

            // D7 셀에 테두리 설정
            Cell cellD7 = rowA7.createCell(3);
            cellD7.setCellStyle(valueStyleTR);

            // D8 셀에 테두리 설정
            Cell cellD8 = rowA8.createCell(3);
            cellD8.setCellStyle(valueStyleRight);

            // D9 셀에 테두리 설정
            Cell cellD9 = rowA9.createCell(3);
            cellD9.setCellStyle(valueStyleRight);

            // D10 셀에 테두리 설정
            Cell cellD10 = rowA10.createCell(3);
            cellD10.setCellStyle(valueStyleRight);

            // D11 셀에 테두리 설정
            Cell cellD11 = rowA11.createCell(3);
            cellD11.setCellStyle(valueStyleRight);

            // D12 셀에 "TOTAL_VALUE" 텍스트 설정
            Cell cellD12 = rowA12.createCell(3);
            cellD12.setCellValue("9. TOTAL VALUE");
            cellD12.setCellStyle(titleStyle2);

            // D13 셀에 단가합계 설정
            Cell cellD13 = rowA13.createCell(3);
            //cellD13.setCellValue(ftaDutyList.getUnitPrice());
            cellD13.setCellValue(goodsPrice);
            cellD13.setCellStyle(valueStyle3);

            // 셀 스타일 생성
            CellStyle borderStyle = workbook.createCellStyle();
            CellStyle rightBorderStyle = workbook.createCellStyle();
            CellStyle topBottomBorderStyle = workbook.createCellStyle();

            // 왼쪽/오른쪽 테두리 설정
            borderStyle.setBorderLeft(BorderStyle.THIN);
            borderStyle.setBorderRight(BorderStyle.THIN);

            // 오른쪽 테두리 설정
            rightBorderStyle.setBorderRight(BorderStyle.THIN);

            // 위아래 테두리 설정
            topBottomBorderStyle.setBorderTop(BorderStyle.THIN);
            topBottomBorderStyle.setBorderBottom(BorderStyle.THIN);
            topBottomBorderStyle.setBorderLeft(BorderStyle.THIN);
            topBottomBorderStyle.setBorderRight(BorderStyle.THIN);

            // A14~D18: A열의 경우만 왼쪽/오른쪽 테두리, 나머지 오른쪽 테두리
            for (int rowNum = 13; rowNum < 18; rowNum++) {
                Row row = sheet.getRow(rowNum); // 이미 생성된 행 가져오기
                if (row == null) {
                    row = sheet.createRow(rowNum);
                }
                for (int colNum = 0; colNum < 4; colNum++) { // A-D 열 (0-3 index)

                    Cell cell = row.getCell(colNum);
                    if (cell == null) {
                        cell = row.createCell(colNum);
                    }
                    if(colNum == 0){
                        cell.setCellStyle(borderStyle);
                    }else {
                        cell.setCellStyle(rightBorderStyle);
                    }
                }
            }

            // A19~D19: 아래위 테두리
            Row row = sheet.createRow(18); // A19는 0-based index로 18번 행
            for (int colNum = 0; colNum < 4; colNum++) { // A-D 열 (0-3 index)

                Cell cell = row.createCell(colNum);
                if (colNum == 1) {
                    cell.setCellValue("TOTAL VALUE");
                    CellStyle style = workbook.createCellStyle();
                    topBottomBorderStyle.setAlignment(HorizontalAlignment.CENTER);
                    style.cloneStyleFrom(topBottomBorderStyle);
                    style.setFont(font2);
                    cell.setCellStyle(style);
                } else if (colNum == 3) {
                    CellStyle style = workbook.createCellStyle();
                    topBottomBorderStyle.setAlignment(HorizontalAlignment.RIGHT);
                    style.cloneStyleFrom(topBottomBorderStyle);
                    style.setDataFormat(cellFormat.getFormat("$#,##0.0")); // 달러표기 서식 설정
                    style.setFont(font3);
                    cell.setCellStyle(style);
                    cell.setCellValue(goodsPrice);
                } else {
                    cell.setCellStyle(topBottomBorderStyle);
                }

            }

            // 이미지 추가
            addImagesToExcel(workbook, sheet);
        }

        return workbook;
    }

    /**
     * 로고 및 사인이미지 set
     * @param workbook
     * @param sheet
     * @throws IOException
     */
    private void addImagesToExcel(Workbook workbook, Sheet sheet) throws IOException {
        addImage(workbook, sheet, excel_gmbh_img, 1, 26, 26, 26);
        addImage(workbook, sheet, excel_sign_img, 3, 26, 26, 26);
    }

    /**
     * 로고 및 사인이미지 추가처리
     * @param workbook
     * @param sheet
     * @param imagePath
     * @param col1
     * @param row1
     * @param col2
     * @param row2
     * @throws IOException
     */
    private void addImage(Workbook workbook, Sheet sheet, String imagePath, int col1, int row1, int col2, int row2) throws IOException {
        ClassPathResource imgFile = new ClassPathResource(imagePath);
        try (InputStream inputStream = imgFile.getInputStream()) {
            byte[] bytes = IOUtils.toByteArray(inputStream);
            int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);

            CreationHelper helper = workbook.getCreationHelper();
            Drawing<?> drawing = sheet.createDrawingPatriarch();
            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setCol1(col1);
            anchor.setRow1(row1);
            anchor.setCol2(col2);
            anchor.setRow2(row2);

            Picture pict = drawing.createPicture(anchor, pictureIdx);
            pict.resize();
        }
    }
}
