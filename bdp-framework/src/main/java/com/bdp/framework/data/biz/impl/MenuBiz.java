package com.bdp.framework.data.biz.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bdp.framework.data.entity.Menu;

@Service
@Transactional(rollbackFor = Exception.class)
public class MenuBiz extends BaseBizImpl<Menu> {

}
