package com.sportsecho.common.redis;

import com.sportsecho.hotdeal.dto.request.PurchaseHotdealRequestDto;
import com.sportsecho.hotdeal.entity.Hotdeal;
import com.sportsecho.hotdeal.event.HotdealPermissionEvent;
import com.sportsecho.member.entity.Member;
import java.time.Duration;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "RedisUtil")
public class RedisUtil {

    private final RedisTemplate<String, String> userRedisTemplate;
    private final RedisTemplate<String, Object> hotdealRedisTemplate;
    private final ApplicationEventPublisher eventPublisher;
    private final StringRedisTemplate stringRedisTemplate;

    private final Long REFRESH_TIME = 7 * 24 * 60 * 60 * 1000L;
    private final int start = 0;

    @Setter
    private int publishedSize;

    // login
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

    // hotdeal v2
    // 넣기
    public void addPurchaseHotdealMemberToQueueString(String queueName, String memberName,
        double score) {
        ZSetOperations<String, String> zSetOperations = stringRedisTemplate.opsForZSet();
        zSetOperations.add(queueName, memberName, score);
    }

    //
    public int getQueueSize(String queueName) {
        ZSetOperations<String, String> zSetOperations = stringRedisTemplate.opsForZSet();
        return Objects.requireNonNull(zSetOperations.size(queueName)).intValue();
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

    // hotdeal v3
    public void addQueue(Long hotdeal, Member user, PurchaseHotdealRequestDto requestDto) {
        final String member = String.valueOf(user.getId());
        String hotdealId = String.valueOf(hotdeal);

        HashOperations<String, String, Object> hashOperations = hotdealRedisTemplate.opsForHash();

        hashOperations.put(member, "hotdeal", hotdealId);
        hashOperations.put(member, "memberName", user.getMemberName());
        hashOperations.put(member, "quantity", requestDto.getQuantity());
        hashOperations.put(member, "address", requestDto.getAddress());
        hashOperations.put(member, "phone", requestDto.getPhone());

        final long now = System.currentTimeMillis();
        hotdealRedisTemplate.opsForZSet().add(hotdealId, member, (int) now);

        //log.info("대기열에 추가 - {} ({}초)", member, now);
    }

    public void waiting(Hotdeal hotdeal) {
        String hotdealId = String.valueOf(hotdeal.getId());

        Set<Object> queue = hotdealRedisTemplate.opsForZSet().range(hotdealId, start, -1);

        for (Object member : queue) {
            Long rank = hotdealRedisTemplate.opsForZSet().rank(hotdealId, member);
            //log.info("{}님의 현재 대기번호는 {}번 입니다.", member, rank);
        }
    }

    public void publish(Hotdeal hotdeal) {
        final long end = publishedSize - 1;
        String hotdealId = String.valueOf(hotdeal.getId());

        Set<Object> queue = hotdealRedisTemplate.opsForZSet().range(hotdealId, start, end);
        HashOperations<String, String, Object> hashOperations = hotdealRedisTemplate.opsForHash();

        for (Object member : queue) {
            if (hotdeal.getDealQuantity() == 0) {
                break;
            }
            hotdealRedisTemplate.opsForZSet().remove(hotdealId, member);
            String memberId = (String) member;

            eventPublisher.publishEvent(new HotdealPermissionEvent(
                hotdeal, Long.parseLong(memberId),
                (int) hashOperations.get(memberId, "quantity"),
                (String) hashOperations.get(memberId, "address"),
                (String) hashOperations.get(memberId, "phone")
            ));
        }
    }

    public void clearQueue(Long hotdealId) {
        hotdealRedisTemplate.opsForZSet().removeRange(String.valueOf(hotdealId), start, -1);
    }

    public Long getSize(Long hotdealId) {
        return hotdealRedisTemplate.opsForZSet().size(String.valueOf(hotdealId));
    }
}