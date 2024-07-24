package com.malltail.erp.service;

import com.malltail.erp.dto.PurchaseExecuteDto;
import com.malltail.erp.mapper.primary.PurchaseExecuteMapper;
import com.malltail.erp.vo.PurchaseExecuteItemVo;
import com.malltail.erp.vo.PurchaseExecuteVo;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PurchaseExecuteService {

    private final PurchaseExecuteMapper purchaseExecuteMapper;

    // 메인항목명
    private static final String[] TITLE_HEADERS = {"번호","MLB_NO","편명(선기명)","출발일","도착일(입항일)","출발지코드","도착지코드","반입일자","검사(반입)장소 보관창고코드","검사(반입)장소 전화번호","검사(반입)장소 보관창고명","CNT","운송장번호","주문번호","받는분이름","받는분휴대폰",
            "개인통관부호","받는분이메일","사이트URL","구매대행URL","구매대행상호","대표자성명","우편번호","기본주소","도로명코드","구매대행자 건물관리번호","구매대행자 상세주소","전화번호","구매대행업등록번호","통신판매신고번호","상품군"};
    // ITEM 항목명
    private static final String[] ITEM_HEADERS = {"ITEM_이름_#CNT#","ITEM_제조사_#CNT#","ITEM_제조국코드_#CNT#","제품URL_#CNT#","ITEM_수량_#CNT#","ITEM_수량단위_#CNT#"};
    private static final int MAX_ITEMS = 30;
    private static final int INITIAL_HEADER_ROW = 0;

    /**
     * 식품신고내역 및 업로드용 엑셀파일 생성 처리.
     * @param purchaseExecuteDto
     * @return
     * @throws IOException
     */
    public Workbook createPurchaseExecuteExcelDown(PurchaseExecuteDto purchaseExecuteDto) throws IOException {

        //엑셀메인항목 데이터 조회
        List<PurchaseExecuteVo> purchaseExecuteVo = purchaseExecuteMapper.PurchaseExecuteExcelList(purchaseExecuteDto);
        //엑셀 고객별 상품정보 조회
        List<PurchaseExecuteItemVo> purchaseExecuteItemVo = purchaseExecuteMapper.PurchaseExecuteExcelItemList(purchaseExecuteDto);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");

        //엑셀 메인헤더 항목명 생성
        createHeaderRow(sheet);
        //개인통관부호를 키로하여 고객별 n개의 상품정보 세팅(ITEM_이름_n,ITEM_제조사_n,ITEM_제조국코드_n,제품URL_n,ITEM_수량_n,ITEM_수량단위_n)
        Map<String, List<PurchaseExecuteItemVo>> itemMap = groupItemsByCustomNum(purchaseExecuteItemVo);
        //엑셀 데이터 생성
        fillSheetWithData(sheet, purchaseExecuteVo, itemMap);

        return workbook;
    }

    /**
     * 엑셀 헤더 항목명 생성
     * @param sheet
     */
    private void createHeaderRow(Sheet sheet){
        Row headerRow = sheet.createRow(INITIAL_HEADER_ROW);
        int titleCnt = TITLE_HEADERS.length;
        //기본 항목명 생성
        for(int i=0;i<titleCnt;i++){
            Cell headerCell = headerRow.createCell(i);
            headerCell.setCellValue(TITLE_HEADERS[i]);
        }
        //고객별 n개의 ITEM항목명 생성
        for(int subCnt=0;subCnt<MAX_ITEMS;subCnt++){
            for(int j=0;j<ITEM_HEADERS.length;j++){
                Cell headerCell = headerRow.createCell(titleCnt+(subCnt * ITEM_HEADERS.length)+j);
                headerCell.setCellValue(ITEM_HEADERS[j].replace("#CNT#",Integer.toString(subCnt+1)));
            }
        }
    }

    /**
     * ITEM 항목별 값세팅
     * 개인통관부호를 키로하여 고객별 ITEM 내용 세팅.
     * @param purchaseExecuteItemVo
     * @return
     */
    private Map<String, List<PurchaseExecuteItemVo>> groupItemsByCustomNum(List<PurchaseExecuteItemVo> purchaseExecuteItemVo){
        Map<String, List<PurchaseExecuteItemVo>> itemMap = new HashMap<>();

        for(PurchaseExecuteItemVo item : purchaseExecuteItemVo){
            itemMap.computeIfAbsent(item.getPrivateCustomNum(), k -> new ArrayList<>()).add(item);
        }

        return itemMap;
    }

    /**
     * 엑셀 데이터생성
     * @param sheet
     * @param purchaseExecuteVo
     * @param itemMap
     */
    private void fillSheetWithData(Sheet sheet, List<PurchaseExecuteVo> purchaseExecuteVo, Map<String, List<PurchaseExecuteItemVo>> itemMap){

        int rowNum = 1;
        String customNumChk = "";

        for(PurchaseExecuteVo fillList : purchaseExecuteVo){
            // n개의 상품을 산 고객은 한 row만 세팅한다.
            if(!customNumChk.equals(fillList.getPrivateCustomNum())){
                //메인 항목에 대한 값세팅
                Row rowValue = createDataRow(sheet, rowNum++, fillList);
                //고객별 n개의 ITEM값 세팅
                fillRowWithItems(rowValue, itemMap.get(fillList.getPrivateCustomNum()));
            }
            //개인통관부호 set(중복 체크용)
            customNumChk = fillList.getPrivateCustomNum();
        }
    }

    /**
     * 고객별 메인항목값 처리
     * @param sheet
     * @param rowNum
     * @param purchaseExecuteVo
     * @return
     */
    private Row createDataRow(Sheet sheet, int rowNum, PurchaseExecuteVo purchaseExecuteVo){

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        // 도착일(입항일) 현재 날짜에 2일을 더함
        String arrivalDate = today.plusDays(2).format(formatter).toString();
        // 반입일자 현재 날짜에 3일을 더함
        String carryDate = today.plusDays(3).format(formatter).toString();

        Row rowData = sheet.createRow(rowNum);
        rowData.createCell(0).setCellValue(rowNum); // 번호
        rowData.createCell(1).setCellValue(""); // MLB_NO
        rowData.createCell(2).setCellValue(""); // 편명(선기명)
        rowData.createCell(3).setCellValue(""); // 출발일
        rowData.createCell(4).setCellValue(arrivalDate); // 도착일(입항일)
        rowData.createCell(5).setCellValue("FRA"); // 출발지코드
        rowData.createCell(6).setCellValue("ICN"); // 도착지코드
        rowData.createCell(7).setCellValue(carryDate); // 반입일자
        rowData.createCell(8).setCellValue("04002549"); // 검사(반입)장소 보관창고코드
        rowData.createCell(9).setCellValue(""); // 검사(반입)장소 전화번호
        rowData.createCell(10).setCellValue("인천공항세관검사장"); // 검사(반입)장소 보관창고명
        rowData.createCell(11).setCellValue(""); // CNT
        rowData.createCell(12).setCellValue(purchaseExecuteVo.getTrackingNum()); // 운송장번호
        rowData.createCell(13).setCellValue(purchaseExecuteVo.getTrackingNum()); // 주문번호
        rowData.createCell(14).setCellValue(purchaseExecuteVo.getReceiverName()); // 받는분이름
        rowData.createCell(15).setCellValue(purchaseExecuteVo.getPhoneNum()); // 받는분휴대폰
        rowData.createCell(16).setCellValue(purchaseExecuteVo.getPrivateCustomNum()); // 개인통관부호
        rowData.createCell(17).setCellValue(""); // 받는분이메일
        rowData.createCell(18).setCellValue(purchaseExecuteVo.getPurUrl()); // 사이트URL
        rowData.createCell(19).setCellValue(purchaseExecuteVo.getMarketName()); // 구매대행URL
        rowData.createCell(20).setCellValue(purchaseExecuteVo.getPurCompanyName()); // 구매대행상호
        rowData.createCell(21).setCellValue(purchaseExecuteVo.getPurPresidentName()); // 대표자성명
        rowData.createCell(22).setCellValue(""); // 우편번호
        rowData.createCell(23).setCellValue(purchaseExecuteVo.getPurAddress()); // 기본주소
        rowData.createCell(24).setCellValue(""); // 도로명코드
        rowData.createCell(25).setCellValue(""); // 구매대행자 건물관리번호
        rowData.createCell(26).setCellValue(""); // 구매대행자 상세주소
        rowData.createCell(27).setCellValue(purchaseExecuteVo.getPurPhoneNum()); // 전화번호
        rowData.createCell(28).setCellValue(purchaseExecuteVo.getPurRegisterNum()); // 구매대행업등록번호
        rowData.createCell(29).setCellValue(purchaseExecuteVo.getPurEleRegisterNum()); // 구매대행업신고번호
        rowData.createCell(30).setCellValue(purchaseExecuteVo.getFoodType()); // 상품군

        return rowData;

    }

    /**
     * 고객별 n개의 ITEM값 처리 (n개 만큼 세팅한다)
     * @param row
     * @param items
     */
    private void fillRowWithItems(Row rowItem, List<PurchaseExecuteItemVo> itemList){
        int titleCnt = TITLE_HEADERS.length;
        if(itemList != null){
            for(int itemCnt=0;itemCnt<itemList.size() && itemCnt < MAX_ITEMS; itemCnt++){
                PurchaseExecuteItemVo executeItem = itemList.get(itemCnt);
                rowItem.createCell(titleCnt + (itemCnt * ITEM_HEADERS.length) + 0).setCellValue(executeItem.getItemName()); //ITEM_이름_n
                rowItem.createCell(titleCnt + (itemCnt * ITEM_HEADERS.length) + 1).setCellValue(executeItem.getMakingCompanyNm()); //ITEM_제조사_n
                rowItem.createCell(titleCnt + (itemCnt * ITEM_HEADERS.length) + 2).setCellValue(executeItem.getMakingNationalCode()); //ITEM_제조국코드_n
                rowItem.createCell(titleCnt + (itemCnt * ITEM_HEADERS.length) + 3).setCellValue(""); //ITEM_URL_n
                rowItem.createCell(titleCnt + (itemCnt * ITEM_HEADERS.length) + 4).setCellValue(executeItem.getItemCnt()); //ITEM_수량_n
                rowItem.createCell(titleCnt + (itemCnt * ITEM_HEADERS.length) + 5).setCellValue("GT"); //ITEM_수량단위_n
            }
        }
    }

}
