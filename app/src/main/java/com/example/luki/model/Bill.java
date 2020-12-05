package com.example.luki.model;

public class Bill {

    private String id;
    private String transferName;
    private String transferDate;
    private String tranferPrice;

    public Bill() {
    }

    public Bill(String id, String transferName, String transferDate, String tranferPrice) {
        this.id = id;
        this.transferName = transferName;
        this.transferDate = transferDate;
        this.tranferPrice = tranferPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTransferName() {
        return transferName;
    }

    public void setTransferName(String transferName) {
        this.transferName = transferName;
    }

    public String getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(String transferDate) {
        this.transferDate = transferDate;
    }

    public String getTranferPrice() {
        return tranferPrice;
    }

    public void setTranferPrice(String tranferPrice) {
        this.tranferPrice = tranferPrice;
    }
}//closes Class Bill
