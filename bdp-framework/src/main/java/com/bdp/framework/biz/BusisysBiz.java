package com.bdp.framework.biz;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bdp.common.data.biz.impl.BaseBizImpl;
import com.bdp.framework.entity.Busisys;

@Service
@Transactional(rollbackFor = Exception.class)
public class BusisysBiz extends BaseBizImpl<Busisys> {

	public Busisys getBusisysByCode(String code) {
		String jpql = "select b from Busisys b where b.code=?";
		Busisys busisys = getEntityByJpql(jpql, code);
		return busisys;
	}

}
