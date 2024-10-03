package com.audition.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.audition.common.exception.SystemException;
import com.audition.integration.AuditionIntegrationClient;
import com.audition.model.AuditionPost;
import com.audition.model.AuditionPostComment;
import com.audition.model.AuditionPostComments;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("PMD.LinguisticNaming")
class AuditionServiceTest {

    @Mock
    private AuditionIntegrationClient auditionIntegrationClient;

    @Test
    void getPostsShouldInvokeAuditionIntegrationClientAndGetPosts() {
        //Arrange
        final AuditionService auditionService = new AuditionService(auditionIntegrationClient);
        final List<AuditionPost> mockPosts = List.of(new AuditionPost(1, 1, "Test title", "Test body"));

        when(auditionIntegrationClient.fetchAllPosts()).thenReturn(mockPosts);

        //Act
        final List<AuditionPost> posts = auditionService.getPosts();

        //Assert
        verify(auditionIntegrationClient).fetchAllPosts();
        assertEquals(mockPosts, posts);
    }

    @Test
    void getPostByIdShouldReturnCorrectPost() {
        //Arrange
        final AuditionService auditionService = new AuditionService(auditionIntegrationClient);
        final AuditionPost mockPost = new AuditionPost(1, 1, "Test title", "Test body");

        when(auditionIntegrationClient.fetchPostById("1")).thenReturn(mockPost);

        //Act
        final AuditionPost post = auditionService.getPostById("1");

        //Assert
        verify(auditionIntegrationClient).fetchPostById("1");
        assertEquals(mockPost, post);
    }

    @Test
    void getPostByIdShouldThrowExceptionWhenNotFound() {
        //Arrange
        final AuditionService auditionService = new AuditionService(auditionIntegrationClient);

        when(auditionIntegrationClient.fetchPostById("99")).thenThrow(new SystemException("Post not found", 404));

        //Act & Assert
        assertThrows(SystemException.class, () -> auditionService.getPostById("99"));
        verify(auditionIntegrationClient).fetchPostById("99");
    }

    @Test
    void getCommentShouldReturnCorrectComments() {
        //Arrange
        final AuditionService auditionService = new AuditionService(auditionIntegrationClient);
        final List<AuditionPostComment> mockComments = List.of(
            new AuditionPostComment(1, 1, "Test name", "test@example.com", "Test comment"));

        when(auditionIntegrationClient.fetchPostCommentList("1")).thenReturn(mockComments);

        //Act
        final List<AuditionPostComment> comments = auditionService.getComments("1");

        //Assert
        verify(auditionIntegrationClient).fetchPostCommentList("1");
        assertEquals(mockComments, comments);
    }

    @Test
    void getPostCommentsShouldReturnCorrectPostAndComments() {
        //Arrange
        final AuditionService auditionService = new AuditionService(auditionIntegrationClient);
        final AuditionPost mockPost = new AuditionPost(1, 1, "Test title", "Test body");
        final List<AuditionPostComment> mockComments = List.of(
            new AuditionPostComment(1, 1, "Test name", "test@example.com", "Test comment"));
        final AuditionPostComments mockPostComments = new AuditionPostComments(mockPost, mockComments);

        when(auditionIntegrationClient.fetchPostComments("1")).thenReturn(mockPostComments);

        //Act
        final AuditionPostComments postComments = auditionService.getPostComments("1");

        //Assert
        verify(auditionIntegrationClient).fetchPostComments("1");
        assertEquals(mockPostComments, postComments);
    }

}