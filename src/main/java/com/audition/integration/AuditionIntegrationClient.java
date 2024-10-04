package com.audition.integration;

import static com.audition.common.Constants.MICROMETER_SPAN_ID;
import static com.audition.common.Constants.MICROMETER_TRACE_ID;
import static com.audition.common.Constants.MM_APP_API_INTEGRATION_METRIC_NAME;
import static com.audition.common.Constants.X_SPAN_ID;
import static com.audition.common.Constants.X_TRACE_ID;

import com.audition.common.exception.SystemException;
import com.audition.common.logging.AuditionLogger;
import com.audition.configuration.integration.AuditionIntegrationClientProperties;
import com.audition.model.AuditionPost;
import com.audition.model.AuditionPostComment;
import com.audition.model.AuditionPostComments;
import com.audition.service.AuditionMeterService;
import java.net.URI;
import java.util.List;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Client for integrating with the Audition API to fetch posts and comments.
 */
@Component
@RequiredArgsConstructor
@SuppressWarnings("PMD.CouplingBetweenObjects")
public class AuditionIntegrationClient {

    private static final Logger LOG = LoggerFactory.getLogger(AuditionIntegrationClient.class);
    private static final ParameterizedTypeReference<List<AuditionPost>> LIST_OF_AUDITION_POSTS_TYPE =
        new ParameterizedTypeReference<>() {
        };
    private static final ParameterizedTypeReference<List<AuditionPostComment>> LIST_OF_AUDITION_POST_COMMENTS_TYPE =
        new ParameterizedTypeReference<>() {
        };
    private final RestTemplate restTemplate;
    private final AuditionLogger auditionLogger;
    private final AuditionIntegrationClientProperties properties;
    private final AuditionMeterService auditionMeterService;

    public List<AuditionPost> fetchAllPosts() {
        auditionLogger.debug(LOG, "Fetching all posts from the target.");

        final MultiValueMap<String, String> headers = getHttpHeaders();
        final HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        return performExchange(properties.getAuditionPostsUri(), HttpMethod.GET, requestEntity,
            LIST_OF_AUDITION_POSTS_TYPE);
    }

    /**
     * Fetches an audition post by its unique identifier.
     *
     * @param postId The unique identifier of the post to be fetched.
     * @return The fetched audition post.
     * @throws SystemException If the post cannot be found.
     */
    public AuditionPost fetchPostById(final String postId) {
        final URI postUri = properties.getAuditionPostUri(postId);

        auditionLogger.debug(LOG, String.format("Fetching all posts for postId %s", postId));

        final MultiValueMap<String, String> headers = getHttpHeaders();
        final HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        return handleNotFoundException(() -> performExchange(postUri, HttpMethod.GET, requestEntity,
            ParameterizedTypeReference.forType(AuditionPost.class)), "Cannot find a Post with id " + postId);
    }

    /**
     * Fetches an audition post along with all its associated comments.
     *
     * @param postId The unique identifier of the post for which comments are to be fetched.
     * @return An object containing the post and its associated comments.
     */
    public AuditionPostComments fetchPostComments(final String postId) {
        auditionLogger.debug(LOG,
            String.format("Fetching post information and all of its comments for postId %s", postId));

        final AuditionPost post = fetchPostById(postId);
        final List<AuditionPostComment> comments = fetchPostCommentList(postId);
        return new AuditionPostComments(post, comments);
    }

    /**
     * Fetches all comments for a specific audition post.
     *
     * @param postId The unique identifier of the post for which comments are to be fetched.
     * @return A list of comments associated with the specified post.
     */
    public List<AuditionPostComment> fetchPostCommentList(final String postId) {
        auditionLogger.debug(LOG, String.format("Fetching all comments for postId %s", postId));

        return fetchCommentsByUri(properties.getAuditionCommentsUri(postId), postId);
    }

    private List<AuditionPostComment> fetchCommentsByUri(final URI uri, final String postId) {
        final MultiValueMap<String, String> headers = getHttpHeaders();
        final HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

        return handleNotFoundException(
            () -> performExchange(uri, HttpMethod.GET, requestEntity, LIST_OF_AUDITION_POST_COMMENTS_TYPE),
            "Cannot find any Comments for postId " + postId);
    }


    private <T> T performExchange(final URI uri, final HttpMethod method, final HttpEntity<?> requestEntity,
        final ParameterizedTypeReference<T> responseType) {
        return restTemplate.exchange(uri, method, requestEntity, responseType).getBody();
    }

    private <T> T handleNotFoundException(final Supplier<T> supplier, final String errorMessage) {
        try {
            return supplier.get();
        } catch (HttpClientErrorException e) {
            final HttpStatusCode statusCode = e.getStatusCode();
            auditionMeterService.record(MM_APP_API_INTEGRATION_METRIC_NAME, null, false, e);
            throw new SystemException(
                errorMessage,
                statusCode == HttpStatus.NOT_FOUND ? "Resource Not Found" : e.getMessage(),
                statusCode.value(),
                e
            );
        }
    }

    private MultiValueMap<String, String> getHttpHeaders() {
        final MultiValueMap<String, String> headers = new HttpHeaders();
        headers.set(X_TRACE_ID, MDC.get(MICROMETER_TRACE_ID));
        headers.set(X_SPAN_ID, MDC.get(MICROMETER_SPAN_ID));
        return headers;
    }
}
