package com.bdp.config.biz;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bdp.common.data.biz.impl.BaseBizImpl;
import com.bdp.config.entity.Property;

@Service
@Transactional(rollbackFor = Exception.class)
public class PropertyBiz extends BaseBizImpl<Property> {

	public List<Property> queryProfile(String application, String profile, String label) {
		String jpql = "SELECT p FROM Property p Where 1=1 ";
		List<String> args = new ArrayList<String>();
		if (StringUtils.isNotEmpty(application)) {
			jpql += " AND p.application=? ";
			args.add(application);
		}
		if (StringUtils.isNotEmpty(profile)) {
			jpql += " AND p.profile=? ";
			args.add(profile);
		}
		if (StringUtils.isNotEmpty(label)) {
			label += " AND p.label=? ";
			args.add(label);
		}
		return getEntitiesByJpql(jpql, args.toArray());
	}

}
