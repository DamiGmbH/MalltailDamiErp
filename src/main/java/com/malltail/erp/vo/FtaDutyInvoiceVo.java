package com.malltail.erp.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FtaDutyInvoiceVo {
    private String releaseDate;
    private int orderNum;
    private int itemCnt;
    private String itemName;
    private Double unitPrice;
    private String receiverName;
    private String address;
    private String phoneNum;
    private String trackingNum;
    private String companyName;
    private String startdate;
    private String enddate;

}
