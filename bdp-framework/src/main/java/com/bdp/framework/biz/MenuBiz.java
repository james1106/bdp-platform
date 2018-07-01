package com.bdp.framework.biz;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bdp.common.data.biz.impl.BaseBizImpl;
import com.bdp.framework.entity.Menu;

@Service
@Transactional(rollbackFor = Exception.class)
public class MenuBiz extends BaseBizImpl<Menu> {

}
