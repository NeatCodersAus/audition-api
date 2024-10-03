package com.audition.service;

import com.audition.integration.AuditionIntegrationClient;
import com.audition.model.AuditionPost;
import com.audition.model.AuditionPostComment;
import com.audition.model.AuditionPostComments;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service class providing operations for fetching audition posts and their comments.
 */
@Service
@RequiredArgsConstructor
public class AuditionService {

    private final AuditionIntegrationClient auditionIntegrationClient;

    /**
     * Fetches all audition posts from the external integration client.
     *
     * @return a list of all fetched audition posts.
     */
    public List<AuditionPost> getPosts() {
        return auditionIntegrationClient.fetchAllPosts();
    }

    /**
     * Retrieves an audition post by its unique identifier.
     *
     * @param postId The unique identifier of the post to be fetched.
     * @return The fetched audition post.
     */
    public AuditionPost getPostById(final String postId) {
        return auditionIntegrationClient.fetchPostById(postId);
    }

    /**
     * Fetches all comments for a specific audition post.
     *
     * @param postId The unique identifier of the post for which comments are to be fetched.
     * @return A list of comments associated with the specified post.
     */
    public List<AuditionPostComment> getComments(final String postId) {
        return auditionIntegrationClient.fetchPostCommentList(postId);
    }

    /**
     * Fetches an audition post along with all its associated comments.
     *
     * @param postId The unique identifier of the post for which comments are to be fetched.
     * @return An object containing the post and its associated comments.
     */
    public AuditionPostComments getPostComments(final String postId) {
        return auditionIntegrationClient.fetchPostComments(postId);
    }

}
