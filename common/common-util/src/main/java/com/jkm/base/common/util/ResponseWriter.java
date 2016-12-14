package com.jkm.base.common.util;

import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by huangwei on 1/30/15.
 */
public final class ResponseWriter {

    private ResponseWriter() {
    }

    /**
     * response 文本
     *
     * @param response
     * @param responseText
     * @throws IOException
     */
    public static void writeTxtResponse(final ServletResponse response, final String responseText) throws IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
        final PrintWriter writer = response.getWriter();
        writer.write(responseText);
        writer.flush();
        writer.close();
    }

    /**
     * response xml
     *
     * @param response
     * @param responseXML
     * @throws IOException
     */
    public static void writeXMLResponse(final ServletResponse response, final String responseXML) throws IOException {
        response.setContentType("text/xml");
        response.setCharacterEncoding("utf-8");
        final PrintWriter writer = response.getWriter();
        writer.write(responseXML);
        writer.flush();
        writer.close();
    }

    /**
     * response json
     *
     * @param response
     * @param responseJson
     * @throws IOException
     */
    public static void writeJsonResponse(final ServletResponse response, final String responseJson) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        final PrintWriter writer = response.getWriter();
        writer.write(responseJson);
        writer.flush();
        writer.close();
    }

    /**
     * response json
     *
     * @param response
     * @param responseObject
     * @param <T>
     * @throws IOException
     */
    public static <T> void writeJsonResponse(final ServletResponse response, final T responseObject) throws IOException {
        final String jsonStr = JsonUtil.toJsonString(responseObject);
        writeJsonResponse(response, jsonStr);
    }


}
