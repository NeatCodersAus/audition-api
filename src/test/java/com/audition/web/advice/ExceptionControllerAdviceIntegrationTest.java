package com.audition.web.advice;

import static com.audition.web.advice.ExceptionControllerAdvice.SYSTEM_EXCEPTION;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.audition.common.exception.SystemException;
import com.audition.common.logging.AuditionLogger;
import com.audition.service.AuditionService;
import java.net.http.HttpConnectTimeoutException;
import java.net.http.HttpTimeoutException;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.client.HttpClientErrorException;

@SpringBootTest
@AutoConfigureMockMvc
@SuppressWarnings("PMD.ExcessiveImports")
class ExceptionControllerAdviceIntegrationTest {

    private static final String API_ERROR_OCCURRED = "API Error Occurred";
    private static final String POSTS_ENDPOINT = "/api/v1/posts";
    public static final String TITLE = "$.title";
    public static final String STATUS = "$.status";
    public static final String DETAIL = "$.detail";
    public static final String INSTANCE = "$.instance";
    public static final String TEST_SYSTEM_EXCEPTION = "Test system exception";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuditionService auditionService;

    @MockBean
    private AuditionLogger logger;

    @BeforeEach
    void setUp() {
        reset(logger);
    }

    @Test
    void shouldHandleHttpClientExceptionAndReturnProblemDetail() throws Exception {

        //Arrange
        when(auditionService.getPosts()).thenThrow(
            new HttpClientErrorException(HttpStatusCode.valueOf(400), "An exception occurred"));

        //Act
        final ResultActions response = mockMvc.perform(get(POSTS_ENDPOINT)
            .contentType(MediaType.APPLICATION_JSON));

        // verify
        response.andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath(TITLE, CoreMatchers.is(API_ERROR_OCCURRED)))
            .andExpect(jsonPath(STATUS, CoreMatchers.is(400)))
            .andExpect(jsonPath(DETAIL, CoreMatchers.is("400 An exception occurred")))
            .andExpect(jsonPath(INSTANCE, CoreMatchers.is(POSTS_ENDPOINT)));

        verify(auditionService, times(1)).getPosts();
        verify(logger, times(1)).logErrorWithException(any(), anyString(), any(HttpClientErrorException.class));
    }

    @Test
    void shouldHandleExceptionAndReturnProblemDetail() throws Exception {
        //Arrange
        doThrow(new RuntimeException("Some serious error occurred")).when(auditionService).getPosts();

        //Act
        final ResultActions response = mockMvc.perform(get(POSTS_ENDPOINT)
            .contentType(MediaType.APPLICATION_JSON));

        // verify
        response.andDo(print())
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath(TITLE, CoreMatchers.is(API_ERROR_OCCURRED)))
            .andExpect(jsonPath(STATUS, CoreMatchers.is(500)))
            .andExpect(jsonPath(DETAIL, CoreMatchers.is("Some serious error occurred")))
            .andExpect(jsonPath(INSTANCE, CoreMatchers.is(POSTS_ENDPOINT)));

        verify(auditionService, times(1)).getPosts();
        verify(logger, times(1))
            .logErrorWithException(any(),
                eq(ExceptionControllerAdvice.GENERAL_EXCEPTION + "Some serious error occurred"),
                any(RuntimeException.class));
    }

    @Test
    void shouldHandleSystemExceptionWithOriginalExceptionAndReturnProblemDetail() throws Exception {
        //Arrange
        doThrow(new SystemException(TEST_SYSTEM_EXCEPTION,
            "Sys exception", HttpStatus.INTERNAL_SERVER_ERROR.value(),
            new HttpConnectTimeoutException("Connection timed out")))
            .when(auditionService).getPosts();

        //Act
        final ResultActions response = mockMvc.perform(get(POSTS_ENDPOINT)
            .contentType(MediaType.APPLICATION_JSON));

        // verify
        response.andDo(print())
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath(TITLE, CoreMatchers.is("Sys exception")))
            .andExpect(jsonPath(STATUS, CoreMatchers.is(500)))
            .andExpect(jsonPath(DETAIL, CoreMatchers.is(TEST_SYSTEM_EXCEPTION + " - Connection timed out")))
            .andExpect(jsonPath(INSTANCE, CoreMatchers.is(POSTS_ENDPOINT)));

        verify(auditionService, times(1)).getPosts();
        verify(logger, times(1))
            .logErrorWithException(any(), eq(SYSTEM_EXCEPTION + TEST_SYSTEM_EXCEPTION), any(SystemException.class));
    }

    @Test
    void shouldHandleSystemExceptionWithNoStatusCodeAndReturnProblemDetailWith500Status() throws Exception {
        //Arrange
        doThrow(new SystemException(TEST_SYSTEM_EXCEPTION,
            "Second system exception",
            new HttpTimeoutException("Http timed out")))
            .when(auditionService).getPosts();

        //Act
        final ResultActions response = mockMvc.perform(get(POSTS_ENDPOINT)
            .contentType(MediaType.APPLICATION_JSON));

        // verify
        response.andDo(print())
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath(TITLE, CoreMatchers.is("Second system exception")))
            .andExpect(jsonPath(DETAIL, CoreMatchers.is("Test system exception - Http timed out")))
            .andExpect(jsonPath(STATUS, CoreMatchers.is(500)))
            .andExpect(jsonPath(INSTANCE, CoreMatchers.is(POSTS_ENDPOINT)));

        verify(auditionService, times(1)).getPosts();
        verify(logger, times(1))
            .logErrorWithException(any(), eq(SYSTEM_EXCEPTION + TEST_SYSTEM_EXCEPTION), any(SystemException.class));
    }

    @Test
    void shouldHandleUnknownSystemExceptionCodeAndReturnProblemDetailWith500StatusCode() throws Exception {
        //Arrange
        //9999 is not a valid http status code
        doThrow(new SystemException(TEST_SYSTEM_EXCEPTION, "Testing non valid http status code", 9999))
            .when(auditionService).getPosts();

        //Act
        final ResultActions response = mockMvc.perform(get(POSTS_ENDPOINT)
            .contentType(MediaType.APPLICATION_JSON));

        // verify
        response.andDo(print())
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath(TITLE, CoreMatchers.is("Testing non valid http status code")))
            .andExpect(jsonPath(STATUS, CoreMatchers.is(500)))
            .andExpect(jsonPath(DETAIL, CoreMatchers.is(TEST_SYSTEM_EXCEPTION)))
            .andExpect(jsonPath(INSTANCE, CoreMatchers.is(POSTS_ENDPOINT)));

        verify(auditionService, times(1)).getPosts();
        verify(logger, times(1))
            .logErrorWithException(any(), eq(SYSTEM_EXCEPTION + TEST_SYSTEM_EXCEPTION), any(SystemException.class));
    }
}