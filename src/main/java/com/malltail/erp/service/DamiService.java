package com.malltail.erp.service;

import com.malltail.erp.mapper.second.DamiMapper;
import com.malltail.erp.vo.DamiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DamiService {

    private final DamiMapper damiMapper;

    public DamiResponse list(){
        return damiMapper.list(1);
    }
}
