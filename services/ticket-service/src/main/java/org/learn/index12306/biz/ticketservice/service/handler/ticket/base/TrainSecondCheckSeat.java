package org.learn.index12306.biz.ticketservice.service.handler.ticket.base;

import org.learn.index12306.framework.statrer.cache.DistributedCache;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 高铁二等座验证座位
 *
 * @author Milk
 * @version 2023/10/14 10:44
 */
public class TrainSecondCheckSeat implements TrainBitMapCheckSeat{

    @Override
    public boolean checkSeat(String key, HashMap<Integer, Integer> convert, DistributedCache distributedCache) {
        boolean flag = false;
        ValueOperations<String, String> opsForValue = ((StringRedisTemplate) distributedCache.getInstance()).opsForValue();
        AtomicInteger matchCount = new AtomicInteger(0);
        for (int i = 0; i < 5; i++) {
            int cnt = 0;
            if (convert.containsKey(i)) {
                for (int j = 0; j < 18; j++) {
                    Boolean bit = opsForValue.getBit(key, i + j * 5);
                    if (null != bit && bit) {
                        cnt = cnt + 1;
                    }
                    if (cnt == convert.get(i)) {
                        matchCount.getAndIncrement();
                        break;
                    }
                }
                if (cnt != convert.get(i)) {
                    break;
                }
            }
            if (matchCount.get() == convert.size()) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    @Override
    public boolean checkChooseSeat(List<String> chooseSeatList, int[][] actualSeats, Map<Character, Integer> SEAT_Y_INT) {
        return false;
    }
}
