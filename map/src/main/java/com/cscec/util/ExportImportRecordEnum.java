package com.cscec.util;

public enum ExportImportRecordEnum {
    name("Monday"), count("Tuesday");
    private final String day;
    ExportImportRecordEnum(String day) {
        this.day = day;
    }
    public String getDay() {
        return day;
    }

}
