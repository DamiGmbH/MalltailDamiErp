package com.malltail.erp.controller;

import com.malltail.erp.dto.PurchaseExecuteDto;
import com.malltail.erp.service.PurchaseExecuteService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.net.URLEncoder;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PurchaseExecuteController {

    private final PurchaseExecuteService purchaseExecuteService;

    /**
     * 식품신고내역 초기화면
     * @param purchaseExecuteDto
     * @param modelAndView
     * @return
     */
    @RequestMapping(value = "/purchase/purchaseExecute.do", method = RequestMethod.GET)
    public ModelAndView purchaseExecute(@ModelAttribute("formData") PurchaseExecuteDto purchaseExecuteDto, ModelAndView modelAndView){
        log.info("purchase view page loading..");
        return modelAndView;
    }

    /**
     * 식품신고 엑셀다운로드
     * @param purchaseExecuteDto
     * @param httpServletResponse
     * @throws IOException
     */
    @RequestMapping(value = "/purchase/purchaseExecuteExcelDown.do", method = RequestMethod.POST)
    public void purchaseExecuteExcelDown(PurchaseExecuteDto purchaseExecuteDto,HttpServletResponse httpServletResponse) throws IOException {
        purchaseExecuteDto.setUploaduse("N");
        createAndWriteExcelFile(purchaseExecuteDto, "식품신고", httpServletResponse);

    }

    /**
     * 식품신고_업로드용 엑셀 다운로드
     * @param purchaseExecuteDto
     * @param httpServletResponse
     * @throws IOException
     */
    @RequestMapping(value = "/purchase/purchaseExecuteExcelDownUpload.do", method = RequestMethod.POST)
    public void purchaseExecuteExcelDownUpload(PurchaseExecuteDto purchaseExecuteDto,HttpServletResponse httpServletResponse) throws IOException {
        purchaseExecuteDto.setUploaduse("Y");
        createAndWriteExcelFile(purchaseExecuteDto, "식품신고_업로드", httpServletResponse);
    }

    /**
     * 식품신고(업로드) 엑셀 다운로드 처리.
     * @param purchaseExecuteDto
     * @param fileName
     * @param httpServletResponse
     * @throws IOException
     */
    private void createAndWriteExcelFile(PurchaseExecuteDto purchaseExecuteDto, String fileName, HttpServletResponse httpServletResponse) throws IOException {

        // 엑셀다운로드 처리
        Workbook workbook = purchaseExecuteService.createPurchaseExecuteExcelDown(purchaseExecuteDto);

        String encodedFileName = URLEncoder.encode(fileName, "UTF-8");

        httpServletResponse.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        httpServletResponse.setHeader("Content-Disposition", "attachment; filename=" + encodedFileName + ".xlsx");

        workbook.write(httpServletResponse.getOutputStream());
        workbook.close();
    }

}