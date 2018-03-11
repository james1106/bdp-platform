package com.bdp.metadata.remoteapi;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 注意，这里如果bdp-metadata这个服务的server.servlet.context-path设置了值metadata ，,
 * 则name需要加上这个值否则无法找到服务。如：bdp-metadata/metadata.
 * 
 * @author jack
 */
@FeignClient(name = "bdp-metadata")
public interface MetadataClient {
	@GetMapping("/dsMetadata/{dsId}")
	public String dsMetadata(@PathVariable("dsId") String dsId);
}
