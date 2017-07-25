package com.jkm.base.common.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;

/**
 * Created by yulong.zhang on 2017/7/24.
 */
@Slf4j
@UtilityClass
public final class ClientSocketUtil {


    /**
     * 读取socket
     *
     * @param socket
     * @return
     */
    public static String readMsg(final Socket socket) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        final StringBuilder msg = new StringBuilder("");
        String temp;
        while ((temp = bufferedReader.readLine()) != null) {
            msg.append(temp);
            if (temp.contains("EOF")) {
                break;
            }
        }
        return msg.toString();
    }

    /**
     * 发消息
     *
     * @param socket
     * @param msg
     */
    public static void sendMsg(final Socket socket, final String msg) throws IOException {
        final BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bufferedWriter.write(msg + "EOF");
        bufferedWriter.flush();
    }

    /**
     * 获取socket
     *
     * @param ip
     * @param port
     * @return
     */
    public static Socket getSocket(final String ip, final int port) {
        for (int i = 3; i > 0; i--) {
            final Socket socket = createSocket(ip, port, i);
            if (null != socket) {
                return socket;
            }
        }
        return null;
    }
    /**
     * 创建socket
     *
     * @param ip
     * @param port
     * @param times
     * @return
     */
    private static Socket createSocket(final String ip, final int port, final int times) {
        try {
            final Socket socket = new Socket(ip, port);
            return socket;
        } catch (final IOException e) {
            log.error("ip-[{}], port-[{}], times-[{}]create socket exception", ip, port, times);
        }
        return null;
    }
}
