package com.sportsecho.member.repository;

import com.sportsecho.member.entity.Member;
import com.sportsecho.member.entity.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    Optional<Member> findBySocialIdAndSocialType(Long socialId, SocialType socialType);
}
