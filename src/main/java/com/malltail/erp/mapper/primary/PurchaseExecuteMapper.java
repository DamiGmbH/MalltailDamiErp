package com.malltail.erp.mapper.primary;

import com.malltail.erp.dto.PurchaseExecuteDto;
import com.malltail.erp.vo.PurchaseExecuteItemVo;
import com.malltail.erp.vo.PurchaseExecuteVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PurchaseExecuteMapper {
    List<PurchaseExecuteVo> PurchaseExecuteExcelList(PurchaseExecuteDto purchaseExecuteDto);

    List<PurchaseExecuteItemVo> PurchaseExecuteExcelItemList(PurchaseExecuteDto purchaseExecuteDto);
}
