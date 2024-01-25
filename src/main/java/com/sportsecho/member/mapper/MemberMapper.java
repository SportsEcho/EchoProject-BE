package com.sportsecho.member.mapper;

import com.sportsecho.member.dto.MemberRequestDto;
import com.sportsecho.member.dto.MemberResponseDto;
import com.sportsecho.member.entity.Member;
import com.sportsecho.member.entity.MemberRole;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

//@Mapper 어노테이션이 있어야 MapStruct을 활용할 수 있다.
@Mapper
public interface MemberMapper {
    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    Member toEntity(MemberRequestDto request, MemberRole role);

    MemberResponseDto toResponseDto(Member member);
}