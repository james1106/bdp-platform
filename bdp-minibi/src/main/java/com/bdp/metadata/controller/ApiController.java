package com.bdp.metadata.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bdp.metadata.remoteapi.DataSourceClient;
import com.bdp.metadata.remoteapi.MetadataClient;

@RestController
@RequestMapping("/")
public class ApiController {

	@Autowired
	private DataSourceClient dataSourceClient;

	@Autowired
	private MetadataClient metadataClient;

	/**
	 * minibi中根据数据源获取ID获取其元数据定义信息
	 * @param dsId
	 * @return
	 */
	@GetMapping("/metadataInfo/{dsId}")
	public String dsMetadata(@PathVariable("dsId") String dsId) {
		String info = metadataClient.dsMetadata(dsId);
		return "Return [" + info + "] detadata info FROM MiniBi.";
	}
	
	/**
	 * minibi中根据数据源获取ID获取其元数据定义信息
	 * @param dsId
	 * @return
	 */
	@GetMapping("/datasource/{dsId}")
	public String datasource(@PathVariable("dsId") String dsId) {
		String info = dataSourceClient.getDataSourceById(dsId);
		return "Return [" + info + "] datasource info FROM MiniBi.";
	}

}
