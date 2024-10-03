package com.audition.integration;

import static com.audition.data.TestData.AUDITION_POST_COMMENT_LIST;
import static com.audition.data.TestData.POST1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

import com.audition.common.exception.SystemException;
import com.audition.common.logging.AuditionLogger;
import com.audition.configuration.integration.AuditionIntegrationClientProperties;
import com.audition.model.AuditionPost;
import com.audition.model.AuditionPostComment;
import com.audition.model.AuditionPostComments;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class AuditionIntegrationClientTest {

    private static final URI POSTS_URI = URI.create("/some/posts/uri");
    private static final URI POST_URI = URI.create("/some/post/uri");
    private static final URI COMMENTS_URI = URI.create("/some/comments/uri");
    private static final URI OTHER_URI = URI.create("/some/other/uri");
    private static final String POST_ID = "some-post-id";

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private AuditionLogger auditionLogger;

    @Mock
    private AuditionIntegrationClientProperties properties;

    private AuditionIntegrationClient createClient() {
        return new AuditionIntegrationClient(restTemplate, auditionLogger, properties);
    }

    @Test
    void fetchAllPostsSuccessfully() {
        //Arrange
        final AuditionIntegrationClient client = createClient();
        when(properties.getAuditionPostsUri()).thenReturn(POSTS_URI);
        final List<AuditionPost> mockPosts = List.of(POST1);
        when(restTemplate.exchange(eq(POSTS_URI), eq(HttpMethod.GET), any(),
            ArgumentMatchers.<ParameterizedTypeReference<List<AuditionPost>>>any()))
            .thenReturn(new ResponseEntity<>(mockPosts, HttpStatus.OK));

        //Act
        final List<AuditionPost> result = client.fetchAllPosts();

        //Assert
        assertEquals(mockPosts, result);
    }

    @Test
    void fetchPostByIdSuccessfully() {
        //Arrange
        final AuditionIntegrationClient client = createClient();
        when(properties.getAuditionPostUri(anyString())).thenReturn(POST_URI);
        final AuditionPost mockPost = POST1;
        when(restTemplate.exchange(eq(POST_URI), eq(HttpMethod.GET), any(),
            ArgumentMatchers.<ParameterizedTypeReference<AuditionPost>>any()))
            .thenReturn(new ResponseEntity<>(mockPost, HttpStatus.OK));

        //Act
        final AuditionPost result = client.fetchPostById(POST_ID);

        //Assert
        assertEquals(mockPost, result);
    }

    @Test
    void fetchPostByIdNotFound() {
        //Arrange
        final AuditionIntegrationClient client = createClient();
        when(properties.getAuditionPostUri(anyString())).thenReturn(POST_URI);
        when(restTemplate.exchange(eq(POST_URI), eq(HttpMethod.GET), any(),
            eq(new ParameterizedTypeReference<AuditionPost>() {
            })))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        //Act and Assert
        assertThrows(SystemException.class, () -> client.fetchPostById(POST_ID));
    }

    @Test
    void fetchPostCommentListSuccessfully() {
        //Arrange
        final AuditionIntegrationClient client = createClient();
        when(properties.getAuditionCommentsUri(anyString())).thenReturn(COMMENTS_URI);
        final List<AuditionPostComment> mockComments = AUDITION_POST_COMMENT_LIST;
        when(restTemplate.exchange(eq(COMMENTS_URI), eq(HttpMethod.GET), any(),
            ArgumentMatchers.<ParameterizedTypeReference<List<AuditionPostComment>>>any()))
            .thenReturn(new ResponseEntity<>(mockComments, HttpStatus.OK));

        //Act
        final List<AuditionPostComment> result = client.fetchPostCommentList(POST_ID);

        //Assert
        assertEquals(mockComments, result);
    }

    @Test
    void fetchPostCommentListNotFound() {
        //Arrange
        final AuditionIntegrationClient client = createClient();
        when(properties.getAuditionCommentsUri(anyString())).thenReturn(COMMENTS_URI);
        when(restTemplate.exchange(eq(COMMENTS_URI), eq(HttpMethod.GET), any(),
            eq(new ParameterizedTypeReference<List<AuditionPostComment>>() {
            })))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        //Act and assert
        assertThrows(SystemException.class, () -> client.fetchPostCommentList(POST_ID));
    }

    @Test
    void fetchPostCommentsSuccessfully() {
        //Arrange
        final AuditionIntegrationClient client = createClient();
        when(properties.getAuditionPostUri(anyString())).thenReturn(POST_URI);
        when(properties.getAuditionCommentsUri(anyString())).thenReturn(OTHER_URI);
        final AuditionPost mockPost = POST1;
        final List<AuditionPostComment> mockComments = AUDITION_POST_COMMENT_LIST;
        when(restTemplate.exchange(eq(POST_URI), eq(HttpMethod.GET), any(),
            ArgumentMatchers.<ParameterizedTypeReference<AuditionPost>>any()))
            .thenReturn(new ResponseEntity<>(mockPost, HttpStatus.OK));

        when(restTemplate.exchange(eq(OTHER_URI), eq(HttpMethod.GET), any(),
            ArgumentMatchers.<ParameterizedTypeReference<List<AuditionPostComment>>>any()))
            .thenReturn(new ResponseEntity<>(mockComments, HttpStatus.OK));

        //Act
        final AuditionPostComments result = client.fetchPostComments(POST_ID);

        //Assert
        assertEquals(mockPost, result.post());
        assertEquals(mockComments, result.comments());
    }

    @Test
    void fetchPostCommentsNotFound() {
        //Arrange
        final AuditionIntegrationClient client = createClient();
        when(properties.getAuditionPostUri(anyString())).thenReturn(POST_URI);
        when(restTemplate.exchange(eq(POST_URI), eq(HttpMethod.GET), any(),
            eq(new ParameterizedTypeReference<AuditionPost>() {
            })))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        //Act and assert
        assertThrows(SystemException.class, () -> client.fetchPostComments(POST_ID));
    }
}