package com.malltail.erp.mapper.primary;

import com.malltail.erp.dto.FtaDutyInvoiceDto;
import com.malltail.erp.vo.FtaDutyInvoiceVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FtaDutyInvoiceMapper {
    List<FtaDutyInvoiceVo> ExcelDownList(FtaDutyInvoiceDto ftaDutyInvoiceDto);
}
