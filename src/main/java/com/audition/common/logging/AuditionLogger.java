package com.audition.common.logging;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;

@Component
public class AuditionLogger {

    private static final String ERROR_OCCURRED = "An error occurred";
    private static final String COMMA_SPACE_SEPARATOR = ", ";

    public void info(final Logger logger, final String message) {
        if (logger.isInfoEnabled()) {
            logger.info(message);
        }
    }

    public void info(final Logger logger, final String message, final Object object) {
        if (logger.isInfoEnabled()) {
            logger.info(message, object);
        }
    }

    public void debug(final Logger logger, final String message) {
        if (logger.isDebugEnabled()) {
            logger.debug(message);
        }
    }

    public void warn(final Logger logger, final String message) {
        if (logger.isWarnEnabled()) {
            logger.warn(message);
        }
    }

    public void error(final Logger logger, final String message) {
        if (logger.isErrorEnabled()) {
            logger.error(message);
        }
    }

    public void logErrorWithException(final Logger logger, final String message, final Exception e) {
        if (logger.isErrorEnabled()) {
            logger.error(message, e);
        }
    }

    public void logStandardProblemDetail(final Logger logger, final ProblemDetail problemDetail, final Exception e) {
        if (logger.isErrorEnabled()) {
            final String message = createStandardProblemDetailMessage(problemDetail);
            logger.error(message, e);
        }
    }

    public void logHttpStatusCodeError(final Logger logger, final String message, final Integer errorCode) {
        if (logger.isErrorEnabled()) {
            logger.error(createBasicErrorResponseMessage(errorCode, message) + "\n");
        }
    }

    private String createStandardProblemDetailMessage(final ProblemDetail standardProblemDetail) {
        return standardProblemDetail == null || StringUtils.isBlank(standardProblemDetail.getDetail())
            ? ERROR_OCCURRED
            : ERROR_OCCURRED
                .concat(COMMA_SPACE_SEPARATOR)
                .concat("Details: ")
                .concat(standardProblemDetail.getDetail());
    }

    private String createBasicErrorResponseMessage(final Integer errorCode, final String message) {
        final StringBuilder errorMessage = new StringBuilder(45);
        errorMessage.append(ERROR_OCCURRED);
        if (errorCode != null) {
            errorMessage.append(COMMA_SPACE_SEPARATOR).append("ERROR_CODE :").append(errorCode);
        }
        if (message != null) {
            errorMessage.append(COMMA_SPACE_SEPARATOR).append("Details :").append(message);
        }
        return errorMessage.toString();
    }
}
