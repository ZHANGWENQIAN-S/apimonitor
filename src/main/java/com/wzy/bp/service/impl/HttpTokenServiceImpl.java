package com.wzy.bp.service.impl;

import com.wzy.bp.dao.HttpTokenMapper;
import com.wzy.bp.service.HttpTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HttpTokenServiceImpl implements HttpTokenService {

    @Autowired
    private HttpTokenMapper httpTokenMapper;

    @Override
    public String getToken() {
        return httpTokenMapper.findToken();
    }
}
