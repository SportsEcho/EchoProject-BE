package com.sportsecho.member.mapper;

import com.sportsecho.member.dto.MemberRequestDto;
import com.sportsecho.member.dto.MemberResponseDto;
import com.sportsecho.member.dto.MemberResponseDto.MemberResponseDtoBuilder;
import com.sportsecho.member.entity.Member;
import com.sportsecho.member.entity.Member.MemberBuilder;
import com.sportsecho.member.entity.MemberRole;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-01-17T20:08:23+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
public class MemberMapperImpl implements MemberMapper {

    @Override
    public Member toEntity(MemberRequestDto request, MemberRole role) {
        if ( request == null && role == null ) {
            return null;
        }

        MemberBuilder member = Member.builder();

        if ( request != null ) {
            member.memberName( request.getMemberName() );
            member.email( request.getEmail() );
            member.password( request.getPassword() );
        }
        if ( role != null ) {
            member.role( role );
        }

        return member.build();
    }

    @Override
    public MemberResponseDto toResponseDto(Member member) {
        if ( member == null ) {
            return null;
        }

        MemberResponseDtoBuilder memberResponseDto = MemberResponseDto.builder();

        memberResponseDto.memberName( member.getMemberName() );
        memberResponseDto.email( member.getEmail() );

        return memberResponseDto.build();
    }
}
