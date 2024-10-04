package com.audition.service;

import static com.audition.common.Constants.MM_CUSTOM_TAG_CORRELATION_ID;
import static com.audition.common.Constants.MM_CUSTOM_TAG_NAME_ERROR_CODE;
import static com.audition.common.Constants.MM_CUSTOM_TAG_NAME_ERROR_DESCRIPTION;
import static com.audition.common.Constants.MM_CUSTOM_TAG_NAME_ERROR_DETAIL;
import static com.audition.common.Constants.MM_CUSTOM_TAG_NAME_ERROR_TITLE;
import static com.audition.common.Constants.MM_CUSTOM_TAG_NAME_EXCEPTION;
import static com.audition.common.Constants.MM_CUSTOM_TAG_NAME_RESULT;
import static com.audition.common.Constants.MM_CUSTOM_TAG_VALUE_RESULT_FAILURE;
import static com.audition.common.Constants.MM_CUSTOM_TAG_VALUE_RESULT_SUCCESS;
import static com.audition.common.Constants.X_TRACE_ID;

import com.audition.common.exception.SystemException;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

/**
 * AuditionMeterService is responsible for recording metrics related to audition operations. It utilizes a MeterRegistry
 * to keep track of various statistics represented by metric names and tags.
 */
@Slf4j
@RequiredArgsConstructor
public class AuditionMeterService {

    private final MeterRegistry meterRegistry;

    /**
     * Records a metric in the MeterRegistry with the specified details. The method collects tags, adds custom tags for
     * correlation and result, and if an exception is provided, adds details about the exception. Finally, it updates
     * the counter metric with the collected tags.
     *
     * @param metricName the name of the metric to be recorded.
     * @param tagsList   a list of tags to be associated with the metric.
     * @param isSuccess  a boolean indicating the success or failure of the operation.
     * @param e          an optional throwable representing an error or exception, if any.
     */
    public void record(final String metricName, final List<Tag> tagsList, final boolean isSuccess, final Throwable e) {
        final List<Tag> tags = new ArrayList<>();

        Stream.ofNullable(tagsList)
            .flatMap(Collection::stream)
            .forEach(tags::add);

        tags.add(tag(MM_CUSTOM_TAG_CORRELATION_ID, MDC.get(X_TRACE_ID)));
        tags.add(tag(MM_CUSTOM_TAG_NAME_RESULT,
            isSuccess ? MM_CUSTOM_TAG_VALUE_RESULT_SUCCESS : MM_CUSTOM_TAG_VALUE_RESULT_FAILURE));

        if (Objects.nonNull(e)) {
            tags.add(tag(MM_CUSTOM_TAG_NAME_EXCEPTION, e.toString()));
            tags.add(tag(MM_CUSTOM_TAG_NAME_ERROR_DESCRIPTION, e.getMessage()));
        }

        if (Objects.nonNull(e) && e instanceof SystemException ex) {
            tags.add(tag(MM_CUSTOM_TAG_NAME_ERROR_CODE, ex.getStatusCode()));
            tags.add(tag(MM_CUSTOM_TAG_NAME_ERROR_DETAIL, ex.getDetail()));
            tags.add(tag(MM_CUSTOM_TAG_NAME_ERROR_TITLE, ex.getTitle()));
        }

        meterRegistry.counter(metricName, tags).increment();
    }

    /**
     * Creates a tag with the provided key and value. The value is converted to a string, and if the value is null, it
     * defaults to "none".
     *
     * @param key   the key for the tag.
     * @param value the value for the tag. If null, the value defaults to "none".
     * @return a new Tag instance with the specified key and value.
     */
    public static Tag tag(final String key, final Object value) {
        return Tag.of(key, getTagValue(value));
    }

    private static String getTagValue(final Object value) {
        return value == null ? "none" : String.valueOf(value);
    }
}
