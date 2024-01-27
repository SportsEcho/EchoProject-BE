package com.sportsecho.common.redis;

import com.sportsecho.hotdeal.dto.request.PurchaseHotdealRequestDto;
import com.sportsecho.hotdeal.entity.Hotdeal;
import com.sportsecho.hotdeal.event.HotdealPermissionEvent;
import com.sportsecho.hotdeal.repository.HotdealRepository;
import com.sportsecho.member.entity.Member;
import java.time.Duration;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "RedisUtil")
public class RedisUtil {

    private final RedisTemplate<String, String> userRedisTemplate;
    private final RedisTemplate<String, Object> hotdeatRedisTemplate;
    private final ApplicationEventPublisher eventPublisher;
    private final HotdealRepository hotdealRepository;

    private final Long REFRESH_TIME = 7 * 24 * 60 * 60 * 1000L;
    private static final long PUBLISH_SIZE = 5;
    private int eventCount = 10;

    public void saveRefreshToken(String refreshToken, String email) {
        //redis에 refreshToken 저장
        userRedisTemplate.opsForValue().set(refreshToken, email);

        //redis에 저장된 refreshToken의 유효시간 설정
        userRedisTemplate.expire(refreshToken, Duration.ofMillis(REFRESH_TIME));
    }

    public String getEmail(String refreshToken) {
        return userRedisTemplate.opsForValue().get(refreshToken);
    }

    public boolean isExist(String refreshToken) {
        return userRedisTemplate.hasKey(refreshToken);
    }

    public void removeToken(String refreshToken) {
        userRedisTemplate.delete(refreshToken);
    }


    public void addQueue(Hotdeal hotdeal, Member user, PurchaseHotdealRequestDto requestDto) {
        final String member = String.valueOf(user.getId());
        String hotdealId = String.valueOf(hotdeal.getId());
        //final String member = Thread.currentThread().getName();

        HashOperations<String, String, Object> hashOperations = hotdeatRedisTemplate.opsForHash();

        hashOperations.put(member, "hotdeal", hotdealId);
        hashOperations.put(member, "memberName", user.getMemberName());
        hashOperations.put(member, "quantity", requestDto.getQuantity());
        hashOperations.put(member, "address", requestDto.getAddress());
        hashOperations.put(member, "phone", requestDto.getPhone());

        final long now = System.currentTimeMillis();
        hotdeatRedisTemplate.opsForZSet().add(hotdealId, member, (int) now);

        log.info("대기열에 추가 - {} ({}초)", member, now);
    }

    public void getPurchase(Long id) {
        final long start = 0;
        final long end = -1;
        String hotdealId = String.valueOf(id);

        Set<Object> queue = hotdeatRedisTemplate.opsForZSet().range(hotdealId, start, end);
        HashOperations<String, String, Object> hashOperations = hotdeatRedisTemplate.opsForHash();

        for (Object member : queue) {
            Long rank = hotdeatRedisTemplate.opsForZSet().rank(hotdealId, member);
            log.info("{}님의 현재 대기번호는 {}번 입니다.", member, rank);
        }
    }

    public void publish(Long id) {
        final long start = 0;
        final long end = PUBLISH_SIZE - 1;
        String hotdealId = String.valueOf(id);

        Set<Object> queue = hotdeatRedisTemplate.opsForZSet().range(hotdealId, start, end);
        HashOperations<String, String, Object> hashOperations = hotdeatRedisTemplate.opsForHash();

        Hotdeal hotdeal = hotdealRepository.findById(id).orElse(null);

        for (Object member : queue) {
            hotdeatRedisTemplate.opsForZSet().remove(hotdealId, member);
            String memberId = (String) member;

            log.info("이벤트 발생");
            eventPublisher.publishEvent(new HotdealPermissionEvent(
                hotdeal,
                Long.parseLong(memberId),
                (int) hashOperations.get(memberId, "quantity"),
                (String) hashOperations.get(memberId, "address"),
                (String) hashOperations.get(memberId, "phone")
            ));

            this.eventCount--;
            log.info("남은 구매 가능 인원 : {}", eventCount);
        }
    }

    public boolean hotdealEnd() {
        return this.eventCount == 0;
    }

    public Long getSize(Long hotdealId) {
        return hotdeatRedisTemplate.opsForZSet().size(String.valueOf(hotdealId));
    }

    public void deleteAll(Long hotdealId) {
        hotdeatRedisTemplate.opsForZSet().removeRange(String.valueOf(hotdealId), 0, -1);
    }

    public Long getRank(Long hotdealId, String memberName) {
        return hotdeatRedisTemplate.opsForZSet()
            .rank(String.valueOf(hotdealId), Thread.currentThread().getName());
    }
}
