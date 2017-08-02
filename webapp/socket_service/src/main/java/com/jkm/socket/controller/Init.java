package com.jkm.socket.controller;

import com.jkm.socket.service.ServerSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by yulong.zhang on 2017/7/27.
 */
@Slf4j
@Component
public class Init {

    @Autowired
    private ServerSocketService serverSocketService;

    @PostConstruct
    public void initSystem() {
        log.info("######################初始化系统--start##########################");
        this.initServerSocket();
        log.info("######################初始化系统--end##########################");
    }

    /**
     * 初始化ServerSocket
     */
    private void initServerSocket() {
        log.info("######################初始化ServerSocket--start##########################");
        new Thread(){
            @Override
            public void run() {
                serverSocketService.init();
            }
        }.start();
        log.info("######################初始化ServerSocket--end##########################");
    }
}
