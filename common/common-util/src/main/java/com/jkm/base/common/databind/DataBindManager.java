package com.jkm.base.common.databind;


import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by huangwei
 */
public class DataBindManager {

    private static final DataBindManager INSTANCE = new DataBindManager();

    private final ConcurrentHashMap<String, DataBind> map = new ConcurrentHashMap<>();

    private DataBindManager() {
    }

    public static DataBindManager getInstance() {
        return INSTANCE;
    }

    public <T> DataBind<T> getDataBind(String bindType) {
        DataBind<T> dataBind = map.get(bindType);
        if (dataBind == null) {
            DataBind bind = new ThreadLocalDataBind();
            dataBind = map.putIfAbsent(bindType, bind);
            if (dataBind == null) {
                dataBind = bind;
            }
        }
        return dataBind;
    }

}
