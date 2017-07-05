package com.jkm.hss.notifier.exception;

/**
 * Created by huangwei on 6/19/15.
 */
public class NoticeSendException extends RuntimeException {

    /**
     * @param message
     * @param cause
     */
    public NoticeSendException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
