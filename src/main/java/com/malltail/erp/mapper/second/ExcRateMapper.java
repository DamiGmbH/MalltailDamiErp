package com.malltail.erp.mapper.second;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExcRateMapper {

    //환율정보
    double excRateUsd();
}
