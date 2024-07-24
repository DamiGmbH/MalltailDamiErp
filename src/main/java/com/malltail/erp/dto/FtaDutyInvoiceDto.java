package com.malltail.erp.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FtaDutyInvoiceDto {
    private String startdate;
    private String enddate;
    private String shipmentcode = "1";
}
