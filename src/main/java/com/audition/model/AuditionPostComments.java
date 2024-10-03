package com.audition.model;

import java.util.List;

public record AuditionPostComments(AuditionPost post, List<AuditionPostComment> comments) {

}