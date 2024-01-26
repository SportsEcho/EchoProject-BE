package com.sportsecho.common.redis;

import java.time.Duration;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "RedisUtil")
public class RedisUtil {

    private final RedisTemplate<String, String> userRedisTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    private final Long REFRESH_TIME = 7 * 24 * 60 * 60 * 1000L;

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

    // 넣기
    public void addPurchaseHotdealMemberToQueueString(String queueName, String memberName, double score) {
        ZSetOperations<String, String> zSetOperations = stringRedisTemplate.opsForZSet();
        zSetOperations.add(queueName, memberName, score);
    }

    //
    public int getQueueSize(String queueName) {
        ZSetOperations<String, String> zSetOperations = stringRedisTemplate.opsForZSet();
        return Objects.requireNonNull(zSetOperations.size(queueName)).intValue();
    }

    // 가장 최근 큐 꺼내서 내 이메일이랑 비교 (이메일은 unique)
    public Set<String> getOldHotdealWait(String queueName) {
        ZSetOperations<String, String> zSetOperations = stringRedisTemplate.opsForZSet();
        return zSetOperations.range(queueName, 0, 0);
    }

    public Set<String> getOldHotdealWaitSet(String queueName, int size) {
        ZSetOperations<String, String> zSetOperations = stringRedisTemplate.opsForZSet();
        return zSetOperations.range(queueName, 0, size - 1); // 남은 인원보다 더 많은 인원을 뽑아도 상관없음
    }

    public void removePurchaseRequestFromQueue(String queueName, String memberEmail) {
        ZSetOperations<String, String> zSetOperations = stringRedisTemplate.opsForZSet();
        zSetOperations.remove(queueName, memberEmail);
    }

    public Boolean deleteOldHotdealWaitSet(String queueName) {
        return stringRedisTemplate.delete(queueName);
    }

    public boolean isInWaitQueue(String queueName, String memberEmail) {
        log.info("queueName: {}, memberEmail: {}", queueName, memberEmail);
        return stringRedisTemplate.opsForZSet().score(queueName, memberEmail) != null;
    }
}
