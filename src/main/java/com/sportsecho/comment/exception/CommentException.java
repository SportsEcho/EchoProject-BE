package com.sportsecho.comment.exception;

import com.sportsecho.global.exception.GlobalException;

public class CommentException extends GlobalException {
    public CommentException(CommentErrorCode errorCode) {
        super(errorCode);
    }
}

