package com.bdp.tx.netty.service;

public interface TxManagerHttpRequestService {

     String httpGet(String url);

     String httpPost(String url, String params);

}
