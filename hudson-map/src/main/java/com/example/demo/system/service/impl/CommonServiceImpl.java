package com.example.demo.system.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.system.mapper.CommonMapper;
import com.example.demo.system.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(rollbackFor=Exception.class)
@Service
public class CommonServiceImpl   implements CommonService {
    @Autowired
    private CommonMapper commonMapper;

    @Override
    public void insertLoginRecord(JSONObject record) {
        commonMapper.insertLoginRecord(record);
    }

    @Override
    public void insertFileRecord(JSONObject record) {
        commonMapper.insertFileRecord(record);
    }

    @Override
    public List<String> selectEnableHeadImg() {
        return commonMapper.selectEnableHeadImg();
    }


}
