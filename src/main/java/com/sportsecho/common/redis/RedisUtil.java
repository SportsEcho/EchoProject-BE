package com.sportsecho.common.redis;

import com.sportsecho.hotdeal.entity.Hotdeal;
import com.sportsecho.member.entity.Member;
import java.time.Duration;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "RedisUtil")
public class RedisUtil {

    private final RedisTemplate<String, String> userRedisTemplate;
    private final RedisTemplate<String, Object> hotdeatRedisTemplate;

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


    public void addQueue(Hotdeal hotdeal, Member user) {
        final String member = user.getMemberName();
        //final String member = Thread.currentThread().getName();
        final long now = System.currentTimeMillis();
        hotdeatRedisTemplate.opsForZSet().add(String.valueOf(hotdeal.getId()), member, (int) now);
        log.info("대기열에 추가 - {} ({}초)", member, now);
    }

    public void getPurchase(Hotdeal hotdeal) {
        final long start = 0;
        final long end = -1;
        String hotdealId = String.valueOf(hotdeal.getId());

        Set<Object> queue = hotdeatRedisTemplate.opsForZSet().range(hotdealId, start, end);

        for (Object member : queue) {
            Long rank = hotdeatRedisTemplate.opsForZSet().rank(hotdealId, member) + 1;
            log.info("{}님의 현재 대기번호는 {}번 입니다.", member, rank);
            this.eventCount--;
        }
    }

    public void publish(Hotdeal hotdeal) {
        final long start = 0;
        final long end = PUBLISH_SIZE - 1;
        String hotdealId = String.valueOf(hotdeal.getId());

        Set<Object> queue = hotdeatRedisTemplate.opsForZSet().range(hotdealId, start, end);

        for (Object member : queue) {
            log.info("{}님 핫딜 상품 구매가 완료되었습니다.", member);
            hotdeatRedisTemplate.opsForZSet().remove(hotdealId, member);
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
