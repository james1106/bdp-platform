package com.bdp.tx.commons.model;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JsonModel extends MapModel{

    public String toJsonString() {
        return JSONObject.toJSONString(this, SerializerFeature.WriteDateUseDateFormat);
    }


}
