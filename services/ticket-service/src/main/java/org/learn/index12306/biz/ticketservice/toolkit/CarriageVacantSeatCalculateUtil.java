package org.learn.index12306.biz.ticketservice.toolkit;

import cn.hutool.core.lang.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * 座位统计工具类
 *
 * @author Milk
 * @version 2023/10/15 19:42
 */
public final class CarriageVacantSeatCalculateUtil {

    /**
     * 空余座位统计方法
     *
     * @param actualSeats 座位状态数组
     * @return 空余座位集合
     */
    public static List<Pair<Integer, Integer>> buildCarriageVacantSeatList2(int[][] actualSeats, int n, int m) {
        List<Pair<Integer, Integer>> vacantSeatList = new ArrayList<>(16);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (actualSeats[i][j] == 0) {
                    vacantSeatList.add(new Pair<>(i, j));
                }
            }
        }
        return vacantSeatList;
    }

}
