package com.sportsecho.member.mapper;

import com.sportsecho.member.dto.MemberRequestDto;
import com.sportsecho.member.dto.MemberResponseDto;
import com.sportsecho.member.entity.Member;
import com.sportsecho.member.entity.MemberRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

//@Mapper 어노테이션이 있어야 MapStruct을 활용할 수 있다.
@Mapper
public interface MemberMapper {
    MemberMapper MAPPER = Mappers.getMapper(MemberMapper.class);

    //MemberRequestDto에는 role 필드가 없으므로 role은 target 설정 후 CUSTOMER로 고정한다.
    @Mapping(target = "role", expression = "java(com.sportsecho.member.entity.MemberRole.CUSTOMER)")
    Member toEntity(MemberRequestDto request);

    MemberResponseDto toResponseDto(Member member);
}