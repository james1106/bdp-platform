package com.bdp.framework.biz;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bdp.common.biz.BaseBiz;
import com.bdp.framework.entity.Menu;
import com.bdp.framework.mapper.MenuMapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class MenuBiz extends BaseBiz<MenuMapper, Menu> {

}
