package com.malltail.erp.mapper.second;

import com.malltail.erp.vo.DamiResponse;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DamiMapper {

    DamiResponse list(int id);
}
