package com.sportsecho.comment.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum CommentErrorCode {
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다"),
    UNAUTHORIZED_COMMENT_ACCESS(HttpStatus.UNAUTHORIZED, "댓글 접근 권한이 없습니다");


    private final HttpStatus status;
    private final String msg;
}
