package com.bdp.tx.framework.utils;

import com.bdp.tx.framework.utils.serializer.ISerializer;
import com.bdp.tx.framework.utils.serializer.ProtostuffSerializer;
import com.bdp.tx.model.TransactionInvocation;
import com.lorne.core.framework.exception.SerializerException;

/**
 * 序列化与反序列化工具类
 */
public class SerializerUtils {


    private static ISerializer serializer = new ProtostuffSerializer();


    public static byte[] serializeTransactionInvocation(TransactionInvocation invocation)   {
        try {
            return serializer.serialize(invocation);
        } catch (SerializerException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static TransactionInvocation parserTransactionInvocation(byte[] value)  {
        try {
            return serializer.deSerialize(value, TransactionInvocation.class);
        } catch (SerializerException e) {
            e.printStackTrace();
            return null;
        }
    }

}
