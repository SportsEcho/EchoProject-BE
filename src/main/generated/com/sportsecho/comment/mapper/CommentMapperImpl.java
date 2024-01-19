package com.sportsecho.comment.mapper;

import com.sportsecho.comment.dto.CommentRequestDto;
import com.sportsecho.comment.dto.CommentResponseDto;
import com.sportsecho.comment.dto.CommentResponseDto.CommentResponseDtoBuilder;
import com.sportsecho.comment.entity.Comment;
import com.sportsecho.comment.entity.Comment.CommentBuilder;
import com.sportsecho.member.entity.Member;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-01-19T13:28:19+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
@Component
public class CommentMapperImpl implements CommentMapper {

    @Override
    public CommentResponseDto commentToCommentResponseDto(Comment comment) {
        if ( comment == null ) {
            return null;
        }

        CommentResponseDtoBuilder commentResponseDto = CommentResponseDto.builder();

        commentResponseDto.id( comment.getId() );
        commentResponseDto.memberName( commentMemberMemberName( comment ) );
        commentResponseDto.createdDate( comment.getCreatedAt() );
        commentResponseDto.content( comment.getContent() );

        return commentResponseDto.build();
    }

    @Override
    public Comment commentRequestDtoToComment(CommentRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        CommentBuilder comment = Comment.builder();

        comment.content( dto.getContent() );

        return comment.build();
    }

    private String commentMemberMemberName(Comment comment) {
        if ( comment == null ) {
            return null;
        }
        Member member = comment.getMember();
        if ( member == null ) {
            return null;
        }
        String memberName = member.getMemberName();
        if ( memberName == null ) {
            return null;
        }
        return memberName;
    }
}
