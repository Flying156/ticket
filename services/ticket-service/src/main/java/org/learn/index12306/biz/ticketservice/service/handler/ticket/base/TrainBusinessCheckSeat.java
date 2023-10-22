package org.learn.index12306.biz.ticketservice.service.handler.ticket.base;

import org.learn.index12306.framework.statrer.cache.DistributedCache;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 高铁商务座验证座位
 *
 * @author Milk
 * @version 2023/10/13 22:38
 */
public class TrainBusinessCheckSeat implements TrainBitMapCheckSeat{

    @Override
    public boolean checkSeat(String key, HashMap<Integer, Integer> convert, DistributedCache distributedCache) {
        boolean flag = false;
        // 这里假定商务座每行有3座，共两行
        ValueOperations<String, String> opsForValue = ((StringRedisTemplate)distributedCache.getInstance()).opsForValue();
        AtomicInteger matchCount = new AtomicInteger(0);
        for(int i = 0; i < 3; i++){
            int cnt = 0;
            if(convert.containsKey(i)){
                for(int j = 0; j < 2; j++){
                    Boolean bit = opsForValue.getBit(key, i + j * 3 );
                    if(null != bit && bit){
                        cnt = cnt + 1;
                    }
                    if(cnt == convert.get(i)){
                        matchCount.getAndIncrement();
                        break;
                    }
                }
                if(cnt != convert.get(i)){
                    break;
                }
            }
            if(matchCount.get() == convert.size()){
                flag = true;
                break;
            }
        }
        return flag;
    }

    @Override
    public boolean checkChooseSeat(List<String> chooseSeatList, int[][] actualSeats, Map<Character, Integer> SEAT_Y_INT) {
        boolean isExists = true;
        for (int i = 0; i < chooseSeatList.size(); i++) {
            if (chooseSeatList.size() == 1) {
                String chooseSeat = chooseSeatList.get(i);
                int seatX = Integer.parseInt(chooseSeat.substring(1));
                int seatY = SEAT_Y_INT.get(chooseSeat.charAt(0));
                if (actualSeats[seatX][seatY] != 0 && actualSeats[1][seatY] != 0) {
                    break;
                }
            } else {
                String chooseSeat = chooseSeatList.get(i);
                int seatX = Integer.parseInt(chooseSeat.substring(1));
                int seatY = SEAT_Y_INT.get(chooseSeat.charAt(0));
                if (actualSeats[seatX][seatY] != 0) {
                    isExists = false;
                    break;
                }
            }
        }
        return isExists;
    }

}
