package com.example.demo.system.service.impl;

import com.example.demo.system.entity.SystemConfig;
import com.example.demo.system.service.SystemConfigService;
import com.example.demo.system.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("systemConfigService")
@Transactional(rollbackFor = { Exception.class})
public class SystemConfigServiceImpl extends BaseServiceImpl<SystemConfig> implements SystemConfigService {

}