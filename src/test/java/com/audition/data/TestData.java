package com.audition.data;

import com.audition.model.AuditionPost;
import com.audition.model.AuditionPostComment;
import com.audition.model.AuditionPostComments;
import java.util.List;

@SuppressWarnings({"PMD.DataClass", "PMD.TestClassWithoutTestCases"})
public class TestData {

    // Sample posts data
    public static final int POST_1_ID = 1;
    public static final int POST_1_USER_ID = 1;
    public static final String POST_1_TITLE = "title1";
    public static final String POST_1_BODY = "body1";
    public static final int COMMENT_1_ID_FOR_POST_1 = 10;
    public static final int COMMENT_2_ID_FOR_POST_1 = 11;
    public static final String COMMENT_1_NAME_FOR_POST_1 = "my comment1";
    public static final String COMMENT_1_EMAIL_FOR_POST_1 = "user1@mailnator.com";
    public static final String COMMENT_1_BODY_FOR_POST_1 = "comment1-body";

    public static final int POST_2_ID = 2;
    public static final int POST_2_USER_ID = 2;
    public static final String POST_2_TITLE = "title2";
    public static final String POST_2_BODY = "body2";
    public static final String COMMENT_2_NAME_FOR_POST_1 = "my comment2";
    public static final String COMMENT_2_EMAIL_FOR_POST_1 = "user2@mailnator.com";
    public static final String COMMENT_2_BODY_FOR_POST_1 = "comment2-body";

    public static final AuditionPost POST1 = new AuditionPost(POST_1_USER_ID, POST_1_ID, POST_1_TITLE, POST_1_BODY);
    public static final AuditionPost POST2 = new AuditionPost(POST_2_USER_ID, POST_2_ID, POST_2_TITLE, POST_2_BODY);
    public static final List<AuditionPost> AUDITION_POST_LIST = List.of(POST1, POST2);

    // Sample comments data
    public static final AuditionPostComment COMMENT1 = new AuditionPostComment(POST_1_ID, COMMENT_1_ID_FOR_POST_1,
        COMMENT_1_NAME_FOR_POST_1, COMMENT_1_EMAIL_FOR_POST_1, COMMENT_1_BODY_FOR_POST_1);
    public static final AuditionPostComment COMMENT2 = new AuditionPostComment(POST_1_ID, COMMENT_2_ID_FOR_POST_1,
        COMMENT_2_NAME_FOR_POST_1, COMMENT_2_EMAIL_FOR_POST_1, COMMENT_2_BODY_FOR_POST_1);

    public static final List<AuditionPostComment> AUDITION_POST_COMMENT_LIST = List.of(COMMENT1, COMMENT2);

    // Sample post comments data
    public static final AuditionPostComments AUDITION_POST_COMMENTS_POST1 = new AuditionPostComments(POST1,
        List.of(COMMENT1, COMMENT2));

}
