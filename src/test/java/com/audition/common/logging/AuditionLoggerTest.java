package com.audition.common.logging;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.http.ProblemDetail;

@ExtendWith(MockitoExtension.class)
class AuditionLoggerTest {

    private static final String TEST_INFO_MESSAGE = "Test info message";
    private static final String TEST_DEBUG_MESSAGE = "Test debug message";
    private static final String TEST_WARN_MESSAGE = "Test warn message";
    private static final String TEST_ERROR_MESSAGE = "Test error message";

    @Mock
    Logger mockLogger;

    @InjectMocks
    AuditionLogger auditionLogger;

    @Test
    void info() {
        //Arrange
        when(mockLogger.isInfoEnabled()).thenReturn(true);

        //Act
        auditionLogger.info(mockLogger, TEST_INFO_MESSAGE);

        //Assert
        verify(mockLogger, times(1)).info(eq(TEST_INFO_MESSAGE));
    }

    @Test
    void infoWithObject() {
        //Arrange
        when(mockLogger.isInfoEnabled()).thenReturn(true);

        final Object mockObject = new Object();
        //Act
        auditionLogger.info(mockLogger, "Test info message with object", mockObject);

        //Assert
        verify(mockLogger, times(1)).info(eq("Test info message with object"), eq(mockObject));
    }

    @Test
    void debug() {
        //Arrange
        when(mockLogger.isDebugEnabled()).thenReturn(true);

        //Act
        auditionLogger.debug(mockLogger, TEST_DEBUG_MESSAGE);

        //Assert
        verify(mockLogger, times(1)).debug(TEST_DEBUG_MESSAGE);
    }

    @Test
    void warn() {
        //Arrange
        when(mockLogger.isWarnEnabled()).thenReturn(true);

        //Act
        auditionLogger.warn(mockLogger, TEST_WARN_MESSAGE);

        //Assert
        verify(mockLogger, times(1)).warn(TEST_WARN_MESSAGE);
    }

    @Test
    void error() {
        //Arrange
        when(mockLogger.isErrorEnabled()).thenReturn(true);

        //Act
        auditionLogger.error(mockLogger, TEST_ERROR_MESSAGE);

        //Assert
        verify(mockLogger, times(1)).error(TEST_ERROR_MESSAGE);
    }

    @Test
    void logErrorWithException() {
        //Arrange
        when(mockLogger.isErrorEnabled()).thenReturn(true);

        final Exception exception = new Exception();

        //Act
        auditionLogger.logErrorWithException(mockLogger, TEST_ERROR_MESSAGE, exception);

        //Assert
        verify(mockLogger, times(1)).error(TEST_ERROR_MESSAGE, exception);
    }

    @Test
    void logStandardProblemDetail() {
        //Arrange
        when(mockLogger.isErrorEnabled()).thenReturn(true);
        final ProblemDetail problemDetail = mock(ProblemDetail.class);
        when(problemDetail.getDetail()).thenReturn("Problem Details");
        final Exception exception = new Exception();

        //Act
        auditionLogger.logStandardProblemDetail(mockLogger, problemDetail, exception);

        //Assert
        verify(mockLogger, times(1))
            .error("An error occurred, Details: Problem Details", exception);
    }

    @Test
    void logHttpStatusCodeError() {
        //Arrange
        when(mockLogger.isErrorEnabled()).thenReturn(true);

        //Act
        auditionLogger.logHttpStatusCodeError(mockLogger, TEST_ERROR_MESSAGE, 500);

        //Assert
        verify(mockLogger, times(1))
            .error("An error occurred, ERROR_CODE :500, Details :Test error message\n");
    }
}