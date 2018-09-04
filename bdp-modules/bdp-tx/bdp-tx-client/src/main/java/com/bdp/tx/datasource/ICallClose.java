package com.bdp.tx.datasource;

public interface ICallClose<T> {
    void close(T resource);
}
