package com.audition.web;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.audition.common.logging.AuditionLogger;
import com.audition.model.AuditionPost;
import com.audition.model.AuditionPostComment;
import com.audition.model.AuditionPostComments;
import com.audition.service.AuditionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for handling requests related to audition posts and their comments.
 */
@Tag(name = "Audition Posts Api", description = "Provides endpoints on accessing posts and comments")
@RestController
@Validated
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class AuditionController {

    private static final Logger LOG = LoggerFactory.getLogger(AuditionController.class);

    private final AuditionService auditionService;
    private final AuditionLogger auditionLogger;

    /**
     * Fetches the list of audition posts, optionally filtered by title and/or userId.
     *
     * @param title  An optional filter to match the title of the posts.
     * @param userId An optional filter to match the userId of the posts.
     * @return A list of filtered audition posts.
     */
    @Operation(summary = "Get all available posts",
        parameters =
            {
                @Parameter(in = ParameterIn.QUERY, name = "title",
                    description = "Audition title to include in search query",
                    schema = @Schema(type = "string")),

                @Parameter(in = ParameterIn.QUERY, name = "userId",
                    description = "Audition userId to include in search query",
                    schema = @Schema(type = "integer"))
            },
        responses = {@ApiResponse(responseCode = "200", description = "Fetch of Audition posts successful")})
    @GetMapping(path = "/posts", produces = APPLICATION_JSON_VALUE)
    public @ResponseBody List<AuditionPost> getPosts(
        @RequestParam(value = "title", required = false) final Optional<String> title,
        @RequestParam(value = "userId", required = false) final Optional<Integer> userId) {

        auditionLogger.info(LOG, "Fetching all posts available from system");
        return auditionService.getPosts().stream()
            .filter(title.isPresent() ? p -> p.title().equals(title.get()) : p -> true) // doing an in-memory filter. must avoid in prod
            .filter(userId.isPresent() ? p -> p.userId() == userId.get() : p -> true) // doing an in-memory filter. must avoid in prod
            .collect(Collectors.toList());
    }

    /**
     * Retrieves an audition post by its unique identifier.
     *
     * @param postId The unique identifier of the post to be fetched.
     * @return The fetched audition post.
     */
    @Operation(summary = "Get Audition Post information for given postId",
        parameters = {@Parameter(in = ParameterIn.PATH, name = "postId", description = "Post Id", required = true)},
        responses = {
            @ApiResponse(responseCode = "202", description = "Fetch of Audition record for given PostId successful."),
            @ApiResponse(responseCode = "404", description = "Audition record for given PostId not found",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Some error occurred. Please contact support",
                content = {@Content(schema = @Schema(implementation = ProblemDetail.class))})})
    @GetMapping(path = "/posts/{postId}", produces = APPLICATION_JSON_VALUE)
    public @ResponseBody AuditionPost getPost(@PathVariable("postId") @Valid final Integer postId) {

        auditionLogger.info(LOG, "Fetching all posts available from system for given postId {}", postId);
        return auditionService.getPostById(String.valueOf(postId));
    }

    /**
     * Retrieves all comments for a specific post identified by the given postId.
     *
     * @param postId The unique identifier of the post for which comments are to be fetched.
     * @return A list of comments associated with the specified post.
     */
    @Operation(summary = "Get Comments for a given Post id",
        parameters = {@Parameter(in = ParameterIn.PATH, name = "postId", description = "Post Id", required = true)},
        responses = {
            @ApiResponse(responseCode = "200", description = "Fetch of Audition comments successful for given Post Id"),
            @ApiResponse(responseCode = "500", description = "Some error occurred. Please contact support",
                content = {@Content(schema = @Schema(implementation = ProblemDetail.class))})})
    @GetMapping(path = "/posts/{postId}/comments", produces = APPLICATION_JSON_VALUE)
    public @ResponseBody List<AuditionPostComment> getComments(
        @PathVariable("postId") @Valid final Integer postId) {

        auditionLogger.info(LOG, "Fetching all post comments from system for given postId {}", postId);
        return auditionService.getComments(String.valueOf(postId));
    }

    /**
     * Retrieves an audition post along with all its associated comments identified by the given postId.
     *
     * @param postId The unique identifier of the post for which comments are to be fetched.
     * @return An object containing the post and its associated comments.
     */
    @Operation(summary = "Get Comments for a given Post id along with Post information",
        parameters = {@Parameter(in = ParameterIn.QUERY, name = "postId", description = "Post Id", required = true)},
        responses = {
            @ApiResponse(responseCode = "200", description = "Fetch of Audition comments successful for given Post Id."),
            @ApiResponse(responseCode = "404", description = "Audition record and or comments for given PostId not found",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Some error occurred. Please contact support",
                content = {@Content(schema = @Schema(implementation = ProblemDetail.class))})})
    @GetMapping(path = "/comments", produces = APPLICATION_JSON_VALUE)
    public @ResponseBody AuditionPostComments getPostComments(
        @RequestParam(value = "postId", required = false) @Valid final Integer postId) {

        auditionLogger.info(LOG, "Fetching all comments from system for given postId {} along with post info", postId);
        return auditionService.getPostComments(String.valueOf(postId));
    }

}
