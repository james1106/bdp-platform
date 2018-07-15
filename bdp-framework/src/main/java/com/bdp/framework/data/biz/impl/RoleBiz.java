package com.bdp.framework.data.biz.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bdp.framework.data.entity.Role;

@Service
@Transactional(rollbackFor = Exception.class)
public class RoleBiz extends BaseBizImpl<Role> {

}
