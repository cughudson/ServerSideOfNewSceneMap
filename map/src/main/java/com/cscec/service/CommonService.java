package com.cscec.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface CommonService {
    void insertLoginRecord(JSONObject json);

    void insertFileRecord(JSONObject record);

    List<String> selectEnableHeadImg();

}
