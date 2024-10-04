package com.audition.web;

import static com.audition.data.TestData.AUDITION_POST_COMMENTS_POST1;
import static com.audition.data.TestData.AUDITION_POST_COMMENT_LIST;
import static com.audition.data.TestData.AUDITION_POST_LIST;
import static com.audition.data.TestData.COMMENT_1_BODY_FOR_POST_1;
import static com.audition.data.TestData.COMMENT_1_EMAIL_FOR_POST_1;
import static com.audition.data.TestData.COMMENT_1_ID_FOR_POST_1;
import static com.audition.data.TestData.COMMENT_1_NAME_FOR_POST_1;
import static com.audition.data.TestData.COMMENT_2_BODY_FOR_POST_1;
import static com.audition.data.TestData.COMMENT_2_EMAIL_FOR_POST_1;
import static com.audition.data.TestData.COMMENT_2_ID_FOR_POST_1;
import static com.audition.data.TestData.COMMENT_2_NAME_FOR_POST_1;
import static com.audition.data.TestData.POST1;
import static com.audition.data.TestData.POST2;
import static com.audition.data.TestData.POST_1_ID;
import static com.audition.data.TestData.POST_1_TITLE;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.audition.common.logging.AuditionLogger;
import com.audition.configuration.AuditionApiProperties;
import com.audition.configuration.HttpRequestLoggingInterceptor;
import com.audition.service.AuditionMeterService;
import com.audition.service.AuditionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(AuditionController.class)
@AutoConfigureMockMvc
@AutoConfigureWebClient
@SuppressWarnings({"PMD.JUnitTestsShouldIncludeAssert", "PMD.ExcessiveImports"})
class AuditionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuditionApiProperties auditionApiProperties;

    @MockBean
    private AuditionMeterService auditionMeterService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private HttpRequestLoggingInterceptor httpRequestLoggingInterceptor;

    @MockBean
    private AuditionLogger auditionLogger;

    @MockBean
    private AuditionService auditionService;

    @Test
    void testGetAuditionPosts() throws Exception {
        //Arrange
        when(auditionService.getPosts()).thenReturn(AUDITION_POST_LIST);

        //Act
        final ResultActions response = mockMvc.perform(get("/api/v1/posts")
            .contentType(MediaType.APPLICATION_JSON));

        // Assert
        response.andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].title", CoreMatchers.is(POST1.title())))
            .andExpect(jsonPath("$[0].userId", CoreMatchers.is(POST1.userId())))
            .andExpect(jsonPath("$[1].title", CoreMatchers.is(POST2.title())))
            .andExpect(jsonPath("$[1].userId", CoreMatchers.is(POST2.userId())));

    }

    @Test
    void testGetPostById() throws Exception {
        //Arrange
        when(auditionService.getPostById(String.valueOf(POST_1_ID))).thenReturn(POST1);

        //Act
        final ResultActions response = mockMvc.perform(get("/api/v1/posts/{postId}", POST_1_ID)
            .contentType(MediaType.APPLICATION_JSON));

        //Assert
        response.andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title", CoreMatchers.is(POST1.title())));
    }

    @Test
    void testGetComments() throws Exception {
        //Arrange
        when(auditionService.getComments(String.valueOf(POST_1_ID))).thenReturn(AUDITION_POST_COMMENT_LIST);

        //Act
        final ResultActions response = mockMvc.perform(get("/api/v1/posts/{postId}/comments", POST_1_ID)
            .contentType(MediaType.APPLICATION_JSON));

        //Assert
        response.andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].name", CoreMatchers.is(COMMENT_1_NAME_FOR_POST_1)));
    }

    @Test
    void testGetPostComments() throws Exception {
        //Arrange
        when(auditionService.getPostComments(String.valueOf(POST_1_ID))).thenReturn(AUDITION_POST_COMMENTS_POST1);

        //Act
        final ResultActions response = mockMvc.perform(get("/api/v1/comments")
            .param("postId", String.valueOf(POST_1_ID))
            .contentType(MediaType.APPLICATION_JSON));

        //Arrange
        response.andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.post.id", CoreMatchers.is(POST_1_ID)))
            .andExpect(jsonPath("$.post.title", CoreMatchers.is(POST_1_TITLE)))
            .andExpect(jsonPath("$.comments", hasSize(2)))

            .andExpect(jsonPath("$.comments[0].postId", CoreMatchers.is(POST_1_ID)))
            .andExpect(jsonPath("$.comments[0].id", CoreMatchers.is(COMMENT_1_ID_FOR_POST_1)))
            .andExpect(jsonPath("$.comments[0].name", CoreMatchers.is(COMMENT_1_NAME_FOR_POST_1)))
            .andExpect(jsonPath("$.comments[0].email", CoreMatchers.is(COMMENT_1_EMAIL_FOR_POST_1)))
            .andExpect(jsonPath("$.comments[0].body", CoreMatchers.is(COMMENT_1_BODY_FOR_POST_1)))

            .andExpect(jsonPath("$.comments[1].id", CoreMatchers.is(COMMENT_2_ID_FOR_POST_1)))
            .andExpect(jsonPath("$.comments[1].name", CoreMatchers.is(COMMENT_2_NAME_FOR_POST_1)))
            .andExpect(jsonPath("$.comments[1].email", CoreMatchers.is(COMMENT_2_EMAIL_FOR_POST_1)))
            .andExpect(jsonPath("$.comments[1].body", CoreMatchers.is(COMMENT_2_BODY_FOR_POST_1)));

    }

}