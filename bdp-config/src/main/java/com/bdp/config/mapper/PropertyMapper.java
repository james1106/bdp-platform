package com.bdp.config.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.bdp.config.entity.Property;

import tk.mybatis.mapper.common.Mapper;

public interface PropertyMapper extends Mapper<Property> {

	List<Property> queryProfile(@Param("application") String application, @Param("profile") String profile,
			@Param("label") String label);

}
