package com.audition.web.advice;

import static com.audition.common.Constants.MM_APP_API_EXCEPTION_HANDLER_ADVICE_METRIC_NAME;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;

import com.audition.common.exception.SystemException;
import com.audition.common.logging.AuditionLogger;
import com.audition.service.AuditionMeterService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


/**
 * ExceptionControllerAdvice is a centralized exception handler for the application. It extends
 * ResponseEntityExceptionHandler to provide default implementations for standard Spring exceptions and also defines
 * custom handlers for specific exceptions.
 *
 * <p>This class uses @ControllerAdvice annotation to enable global exception handling and the @ExceptionHandler
 * annotation to define methods that handle specific exceptions.
 *
 * <p>This advice component addresses exceptions related to HttpClient errors, general exceptions, and custom
 * SystemExceptions. Each handler method logs the error and returns a ProblemDetail object describing the error,
 * including an appropriate HTTP status code.
 *
 * <p>Dependencies: - AuditionLogger: A custom logger component for enhanced logging capabilities.
 */
@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionControllerAdvice.class);
    public static final String DEFAULT_TITLE = "API Error Occurred";
    private static final String ERROR_MESSAGE = "Error Code from Exception could not be mapped to a valid HttpStatus Code - ";
    private static final String DEFAULT_MESSAGE = "API Error occurred. Please contact support or administrator.";
    private static final String HTTP_CLIENT_EXCEPTION = "Http Client Exception occurred.";
    public static final String GENERAL_EXCEPTION = "An exception occurred.";
    public static final String SYSTEM_EXCEPTION = "System Exception occurred.";

    private final AuditionLogger logger;
    private final AuditionMeterService auditionMeterService;


    /**
     * Handles exceptions of type {@link HttpClientErrorException} thrown by HTTP clients. Logs the exception and
     * creates a {@link ProblemDetail} object to provide more information about the error.
     *
     * @param clientErrorException the instance of {@link HttpClientErrorException} to be handled
     * @return an instance of {@link ProblemDetail} containing error details extracted from the exception
     */
    @ExceptionHandler(HttpClientErrorException.class)
    ProblemDetail handleHttpClientException(final HttpClientErrorException clientErrorException) {
        auditionMeterService.record(MM_APP_API_EXCEPTION_HANDLER_ADVICE_METRIC_NAME, null, false, clientErrorException);
        logger.logErrorWithException(LOG, HTTP_CLIENT_EXCEPTION + clientErrorException.getMessage(),
            clientErrorException);
        return createProblemDetail(clientErrorException, clientErrorException.getStatusCode());

    }

    /**
     * Handles general exceptions thrown by the application. Logs the exception and creates a {@link ProblemDetail}
     * object to provide more information about the error.
     *
     * @param exception the instance of {@link Exception} to be handled
     * @return an instance of {@link ProblemDetail} containing error details extracted from the exception
     */
    @ExceptionHandler(Exception.class)
    ProblemDetail handleMainException(final Exception exception) {

        auditionMeterService.record(MM_APP_API_EXCEPTION_HANDLER_ADVICE_METRIC_NAME, null, false, exception);
        logger.logErrorWithException(LOG, GENERAL_EXCEPTION + exception.getMessage(), exception);

        final HttpStatusCode status = getHttpStatusCodeFromException(exception);
        return createProblemDetail(exception, status);

    }

    /**
     * Handles exceptions of type {@link SystemException} thrown by the system. Logs the exception and creates a
     * {@link ProblemDetail} object to provide more information about the error.
     *
     * @param systemException the instance of {@link SystemException} to be handled
     * @return an instance of {@link ProblemDetail} containing error details extracted from the exception
     */
    @ExceptionHandler(SystemException.class)
    ProblemDetail handleSystemException(final SystemException systemException) {

        auditionMeterService.record(MM_APP_API_EXCEPTION_HANDLER_ADVICE_METRIC_NAME, null, false, systemException);
        logger.logErrorWithException(LOG, SYSTEM_EXCEPTION + systemException.getMessage(), systemException);

        final HttpStatusCode status = getHttpStatusCodeFromSystemException(systemException);
        return createProblemDetail(systemException, status);

    }

    private ProblemDetail createProblemDetail(final Exception exception,
        final HttpStatusCode statusCode) {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(statusCode);
        problemDetail.setDetail(getMessageFromException(exception));
        problemDetail.setTitle(exception instanceof SystemException se ? se.getTitle() : DEFAULT_TITLE);

        return problemDetail;
    }

    private String getMessageFromException(final Exception exception) {
        if (exception instanceof SystemException se) {
            return se.getDetail().concat(se.getCause() != null ? " - " + se.getCause().getMessage() : "");
        }
        if (StringUtils.isNotBlank(exception.getMessage())) {
            return exception.getMessage();
        }
        return DEFAULT_MESSAGE;
    }

    private HttpStatusCode getHttpStatusCodeFromSystemException(final SystemException exception) {
        try {
            return HttpStatusCode.valueOf(exception.getStatusCode());
        } catch (final IllegalArgumentException iae) {
            logger.info(LOG, ERROR_MESSAGE + exception.getStatusCode());
            return INTERNAL_SERVER_ERROR;
        }
    }

    private HttpStatusCode getHttpStatusCodeFromException(final Exception exception) {
        if (exception instanceof HttpClientErrorException) {
            return ((HttpClientErrorException) exception).getStatusCode();
        } else if (exception instanceof HttpRequestMethodNotSupportedException) {
            return METHOD_NOT_ALLOWED;
        }
        return INTERNAL_SERVER_ERROR;
    }
}



