package com.example.firebasedemo;

public class AdminInfo {
    String name, contact, open,close,address;

    public AdminInfo() {
    }

    public AdminInfo(String name, String contact, String open, String close, String address) {
        this.name = name;
        this.contact = contact;
        this.open = open;
        this.close = close;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
