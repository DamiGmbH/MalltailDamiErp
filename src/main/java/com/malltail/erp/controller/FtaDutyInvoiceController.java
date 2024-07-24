package com.malltail.erp.controller;

import com.malltail.erp.dto.FtaDutyInvoiceDto;
import com.malltail.erp.dto.MessageDto;
import com.malltail.erp.service.FtaDutyInvoiceExcelDownService;
import com.malltail.erp.vo.FtaDutyInvoiceVo;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class FtaDutyInvoiceController {

    private final FtaDutyInvoiceExcelDownService ftaDutyInvoiceExcelDownService;

    /**
     * FTA면세인보이스 초기화면
     * @param ftaDutyInvoiceDto
     * @param modelAndView
     * @return
     */
    @RequestMapping(value = "/ftainvoice/dutyInvoice.do", method = RequestMethod.GET)
    public ModelAndView ftaDutyInvoiceExcelDown1(@ModelAttribute("formData") FtaDutyInvoiceDto ftaDutyInvoiceDto,
                                                 ModelAndView modelAndView){

        log.info("default view page loading..");

        return modelAndView;
    }

    /**
     * FTA면세인보이스 엑셀생성 및 다운로드
     * @param ftaDutyInvoiceDto
     * @param model
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/ftainvoice/dutyInvoiceDown.do", method = RequestMethod.POST)
    public String createFtaDutyInvoiceExcel(@ModelAttribute("formData") FtaDutyInvoiceDto ftaDutyInvoiceDto,
                                          Model model, HttpServletResponse response) throws IOException {

        //엑셀 생성 데이터 조회
        List<FtaDutyInvoiceVo> ftaDutyInvoiceResponse = ftaDutyInvoiceExcelDownService.ftaDutyInvoiceList(ftaDutyInvoiceDto);

        MessageDto message = null;

        if(ftaDutyInvoiceResponse.size() > 0) {
            //엑셀 파일 생성
            Workbook workbook = ftaDutyInvoiceExcelDownService.createFtaDutyInvoiceExcelDown(ftaDutyInvoiceResponse);

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=FTA_Duty_Invoice.xlsx");

            workbook.write(response.getOutputStream());
            workbook.close();
        }else{
             message = new MessageDto("내역이 없습니다.","/ftainvoice/dutyInvoice.do",RequestMethod.GET,null);
            return showMessageAndRedirect(message, model);
        }

        return null;
    }

    /**
     * 메세징 처리
     * @param params
     * @param model
     * @return
     */
    private String showMessageAndRedirect(final MessageDto params, Model model){
        model.addAttribute("params",params);
        return "common/messageRedirect";
    }
}
