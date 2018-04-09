package com.bdp.framework.biz;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bdp.common.biz.BaseBiz;
import com.bdp.framework.entity.BusiSys;
import com.bdp.framework.mapper.BusiSysMapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class BusiSysBiz extends BaseBiz<BusiSysMapper, BusiSys> {

}
