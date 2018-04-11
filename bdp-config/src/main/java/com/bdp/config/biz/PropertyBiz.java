package com.bdp.config.biz;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bdp.common.biz.BaseBiz;
import com.bdp.config.entity.Property;
import com.bdp.config.mapper.PropertyMapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class PropertyBiz extends BaseBiz<PropertyMapper, Property> {

	public List<Property> queryProfile(String application, String profile, String label) {
		return mapper.queryProfile(application, profile, label);
	}

}
