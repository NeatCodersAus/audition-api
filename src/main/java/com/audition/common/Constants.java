package com.audition.common;


/**
 * A class to hold constant values used throughout the application.
 */
@SuppressWarnings("PMD.DataClass")
public class Constants {

    public static final String YEAR_MONTH_DAY_PATTERN = "yyyy-MM-dd";

    public static final String X_TRACE_ID = "X-TRACE-ID";
    public static final String X_SPAN_ID = "X-SPAN-ID";
    public static final String MICROMETER_TRACE_ID = "traceId";
    public static final String MICROMETER_SPAN_ID = "spanId";

    //Micrometer tags
    public static final String MM_APP_API_INTEGRATION_METRIC_NAME = "audition.api.integration.client";
    public static final String MM_APP_API_EXCEPTION_HANDLER_ADVICE_METRIC_NAME = "audition.api.exception.handler";
    public static final String MM_CUSTOM_TAG_NAME_ENV = "environment";
    public static final String MM_CUSTOM_TAG_NAME_EXCEPTION = "exception";
    public static final String MM_CUSTOM_TAG_CORRELATION_ID = "correlationId";
    public static final String MM_CUSTOM_TAG_VALUE_RESULT_SUCCESS = "success";
    public static final String MM_CUSTOM_TAG_VALUE_RESULT_FAILURE = "failure";
    public static final String MM_CUSTOM_TAG_NAME_RESULT = "result";
    public static final String MM_CUSTOM_TAG_NAME_ERROR_DESCRIPTION = "error_description";
    public static final String MM_CUSTOM_TAG_NAME_ERROR_CODE = "error_code";
    public static final String MM_CUSTOM_TAG_NAME_ERROR_DETAIL = "error_detail";
    public static final String MM_CUSTOM_TAG_NAME_ERROR_TITLE = "error_title";

}
