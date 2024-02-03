package com.sportsecho.hotdeal.scheduler;

import com.sportsecho.common.exception.GlobalException;
import com.sportsecho.common.redis.RedisUtil;
import com.sportsecho.hotdeal.entity.Hotdeal;
import com.sportsecho.hotdeal.exception.HotdealErrorCode;
import com.sportsecho.hotdeal.repository.HotdealRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class HotdealScheduler {

    private final HotdealRepository hotdealRepository;
    private final RedisUtil redisUtil;

    @Setter
    private Long hotdealId;

    // 매분마다 시행
//    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void deleteClosedHotdeal() {
        LocalDateTime now = LocalDateTime.now();
        List<Hotdeal> expiredHotdeals = hotdealRepository.findAllByDueDayBefore(now);

        if (!expiredHotdeals.isEmpty()) {
            log.info("마감시간이 지난 HOTDEAL {}개를 삭제하겠습니다.", expiredHotdeals.size());
            hotdealRepository.deleteAll(expiredHotdeals);
            hotdealRepository.flush();
            List<Hotdeal> expiredHotdealsForCheck = hotdealRepository.findAllByDueDayBefore(now);
            for (Hotdeal hotdeal : expiredHotdealsForCheck) {
                redisUtil.deleteOldHotdealWaitSet(String.valueOf(hotdeal.getId()));
            }
        }

        List<Hotdeal> hotdealsWithZeroQuantity = hotdealRepository.findAllByDealQuantity(0);

        if (!hotdealsWithZeroQuantity.isEmpty()) {
            log.info("한정수령이 모두 판매된 Hotdeal {}개를 삭제하겠습니다.", hotdealsWithZeroQuantity.size());
            hotdealRepository.deleteAll(hotdealsWithZeroQuantity);
            for (Hotdeal hotdeal : hotdealsWithZeroQuantity) {
                redisUtil.deleteOldHotdealWaitSet(String.valueOf(hotdeal.getId()));
            }
            hotdealRepository.flush();
        }
    }

    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void hotdealEventScheduler() {
        //log.info("==== Hotdeal 이벤트 스케줄러 실행 =====");

        if (hotdealId == null) {
            return;
        }

        Hotdeal hotdeal = hotdealRepository.findByIdWithPessimisticWriteLock(hotdealId)
            .orElseThrow(() -> new GlobalException(HotdealErrorCode.NOT_FOUND_HOTDEAL));

        //log.info("남은 핫딜 수량 : {}", hotdeal.getDealQuantity());
        if (hotdeal.getDealQuantity() == 0) {
            log.info("===== 이벤트가 종료되었습니다. =====");
            redisUtil.clearQueue(hotdeal.getId());
            this.hotdealId = null;
            return;
        }

        redisUtil.publish(hotdeal);
        redisUtil.waiting(hotdeal);
    }
}
