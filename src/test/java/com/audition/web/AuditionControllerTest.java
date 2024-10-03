package com.audition.web;

import static com.audition.data.TestData.AUDITION_POST_COMMENTS_POST1;
import static com.audition.data.TestData.AUDITION_POST_COMMENT_LIST;
import static com.audition.data.TestData.AUDITION_POST_LIST;
import static com.audition.data.TestData.COMMENT_1_ID_FOR_POST_1;
import static com.audition.data.TestData.COMMENT_1_NAME_FOR_POST_1;
import static com.audition.data.TestData.COMMENT_2_ID_FOR_POST_1;
import static com.audition.data.TestData.COMMENT_2_NAME_FOR_POST_1;
import static com.audition.data.TestData.POST1;
import static com.audition.data.TestData.POST2;
import static com.audition.data.TestData.POST_1_ID;
import static com.audition.data.TestData.POST_1_TITLE;
import static com.audition.data.TestData.POST_2_TITLE;
import static org.assertj.core.api.AssertionsForClassTypes.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.audition.common.logging.AuditionLogger;
import com.audition.model.AuditionPost;
import com.audition.model.AuditionPostComment;
import com.audition.model.AuditionPostComments;
import com.audition.service.AuditionService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings({"PMD.ExcessiveImports"})
class AuditionControllerTest {

    @Mock
    private AuditionService auditionService;

    @Mock
    private AuditionLogger auditionLogger;

    @InjectMocks
    private AuditionController auditionController;

    @Test
    void testGetPostsNoParams() {
        //Arrange
        when(auditionService.getPosts()).thenReturn(AUDITION_POST_LIST);

        //Act
        final List<AuditionPost> posts = auditionController.getPosts(Optional.empty(), Optional.empty());

        //Assert
        assertEquals(2, posts.size());
    }

    @Test
    void testGetPostsWithUserId() {

        //Arrange
        when(auditionService.getPosts()).thenReturn(AUDITION_POST_LIST);

        //Act
        final List<AuditionPost> filteredPosts = auditionController.getPosts(Optional.empty(),
            Optional.of(POST1.userId()));

        //Assert
        final AuditionPost firstPost = filteredPosts.stream().findFirst().orElseThrow();

        assertEquals(1, filteredPosts.size());
        assertEquals(POST1.title(), firstPost.title());
    }

    @Test
    void testGetPostsWithTitle() {
        //Arrange
        when(auditionService.getPosts()).thenReturn(AUDITION_POST_LIST);

        //Act
        final List<AuditionPost> posts = auditionController.getPosts(Optional.of(POST2.title()), Optional.empty());

        //Assert
        assertEquals(1, posts.size(), "Expected exactly one post with the given title");

        posts.stream().findFirst().ifPresentOrElse(
            post -> assertEquals(POST_2_TITLE, post.title(), "Post title does not match the expected value"),
            () -> fail("Expected post not found")
        );
    }


    @Test
    void testGetPostWithPostId() {
        //Arrange
        when(auditionService.getPostById(String.valueOf(POST_1_ID))).thenReturn(POST1);

        //Act
        final AuditionPost post = auditionController.getPost(1);

        //Assert
        assertEquals(POST_1_TITLE, post.title());
    }

    @Test
    void testGetComments() {
        //Arrange
        when(auditionService.getComments(String.valueOf(POST_1_ID))).thenReturn(AUDITION_POST_COMMENT_LIST);

        //Act
        final List<AuditionPostComment> postComments = auditionController.getComments(POST_1_ID);

        //Assert
        assertEquals(2, postComments.size());
        assertEquals(POST_1_ID, postComments.get(0).postId());
        assertEquals(COMMENT_1_ID_FOR_POST_1, postComments.get(0).id());
        assertEquals(COMMENT_1_NAME_FOR_POST_1, postComments.get(0).name());

        assertEquals(POST_1_ID, postComments.get(1).postId());
        assertEquals(COMMENT_2_ID_FOR_POST_1, postComments.get(1).id());

    }

    @Test
    void testGetPostComments() {
        //Arrange
        when(auditionService.getPostComments(String.valueOf(POST_1_ID))).thenReturn(AUDITION_POST_COMMENTS_POST1);

        //Act
        final AuditionPostComments result = auditionController.getPostComments(POST_1_ID);

        //Assert
        assertEquals(POST_1_TITLE, result.post().title());
        assertEquals(2, result.comments().size());

        assertEquals(COMMENT_1_ID_FOR_POST_1, result.comments().get(0).id());
        assertEquals(COMMENT_1_NAME_FOR_POST_1, result.comments().get(0).name());

        assertEquals(COMMENT_2_ID_FOR_POST_1, result.comments().get(1).id());
        assertEquals(COMMENT_2_NAME_FOR_POST_1, result.comments().get(1).name());

    }

}