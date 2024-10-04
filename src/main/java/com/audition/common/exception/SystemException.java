package com.audition.common.exception;

import lombok.Getter;

/**
 * SystemException is a custom runtime exception indicating errors within the API system. It extends the
 * RuntimeException class, providing additional information such as status code, title, and detail messages.
 */
@Getter
public class SystemException extends RuntimeException {

    private static final long serialVersionUID = -5876728854007114881L;

    public static final String DEFAULT_TITLE = "API Error Occurred";
    private Integer statusCode;
    private String title;
    private String detail;

    public SystemException() {
        super();
    }

    /**
     * Constructs a new SystemException with the specified detail message.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the
     *                {@link Throwable#getMessage()} method.
     */
    public SystemException(final String message) {
        super(message);
        this.title = DEFAULT_TITLE;
    }

    /**
     * Constructs a new SystemException with the specified detail message and error code.
     *
     * @param message   the detail message. The detail message is saved for later retrieval by the
     *                  {@link Throwable#getMessage()} method.
     * @param errorCode the error code. The error code indicates the type of error occurred and is saved for later
     *                  retrieval by the {@link #getStatusCode()} method.
     */
    public SystemException(final String message, final Integer errorCode) {
        super(message);
        this.title = DEFAULT_TITLE;
        this.statusCode = errorCode;
    }

    /**
     * Constructs a new SystemException with the specified detail message and cause.
     *
     * @param message   the detail message.
     * @param exception the cause.
     */
    public SystemException(final String message, final Throwable exception) {
        super(message, exception);
        this.title = DEFAULT_TITLE;
    }

    /**
     * Constructs a new SystemException with the specified detail message, title, and error code.
     *
     * @param detail    the detail message. The detail message provides additional information. method.
     * @param title     the title of the exception. The title provides a short description or summary of the error.
     * @param errorCode the error code. The error code indicates the type of error occurred.
     */
    public SystemException(final String detail, final String title, final Integer errorCode) {
        super(detail);
        this.statusCode = errorCode;
        this.title = title;
        this.detail = detail;
    }

    /**
     * Constructs a new SystemException with the specified detail message, title, and cause.
     *
     * @param detail    the detail message. The detail message provides additional information about the error.
     * @param title     the title of the exception. The title provides a short description or summary of the error.
     * @param exception the cause of the exception.
     */
    public SystemException(final String detail, final String title, final Throwable exception) {
        super(detail, exception);
        this.title = title;
        this.statusCode = 500;
        this.detail = detail;
    }

    /**
     * Constructs a new SystemException with the specified detail message, error code, and cause.
     *
     * @param detail    the detail message. The detail message provides additional information about the error.
     * @param errorCode the error code. The error code indicates the type of error that occurred and is saved for later
     *                  retrieval.
     * @param exception the cause of the exception.
     */
    public SystemException(final String detail, final Integer errorCode, final Throwable exception) {
        super(detail, exception);
        this.statusCode = errorCode;
        this.title = DEFAULT_TITLE;
        this.detail = detail;
    }

    /**
     * Constructs a new SystemException with the specified detail message, title, error code, and cause.
     *
     * @param detail    the detail message. The detail message provides additional information about the error.
     * @param title     the title of the exception. The title provides a short description or summary of the error.
     * @param errorCode the error code. The error code indicates the type of error that occurred.
     * @param exception the cause of the exception.
     */
    public SystemException(final String detail, final String title, final Integer errorCode,
        final Throwable exception) {
        super(detail, exception);
        this.statusCode = errorCode;
        this.title = title;
        this.detail = detail;
    }
}
