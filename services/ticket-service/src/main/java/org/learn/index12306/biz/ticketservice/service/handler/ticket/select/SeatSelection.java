package org.learn.index12306.biz.ticketservice.service.handler.ticket.select;

import cn.hutool.core.collection.CollUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 座位选择器
 *
 * @author Milk
 * @version 2023/10/15 21:09
 */
public class SeatSelection {

    /**
     * 分配相邻座位
     *
     * @param numSeats   需要的总数
     * @param seatLayout 座位排布
     * @return 分配的座位表
     */
    public static int[][] adjacent(int numSeats, int [][]seatLayout){
        int numsRows = seatLayout.length;
        int numsCols = seatLayout[0].length;
        List<int[]> selectedSeats = new ArrayList<>();
        for(int i = 0; i < numsRows; i++){
            for(int j = 0; j < numsCols; j++){
                if(seatLayout[i][j] == 0){
                    int consecutiveSeats = 0;
                    for(int k = j; k < numsCols; k++){
                        if(seatLayout[i][k] == 0){
                            consecutiveSeats++;
                            // 添加座位坐标
                            if(consecutiveSeats == numSeats){
                                for(int l = k - numSeats + 1; l <= k; l++){
                                    selectedSeats.add(new int[]{i, l});
                                }
                                break;
                            }
                        }else{
                            consecutiveSeats = 0;
                        }
                    }
                    if(!selectedSeats.isEmpty()){
                        break;
                    }
                }
            }
        }
        if(CollUtil.isEmpty(selectedSeats)){
            return null;
        }
        return convertToActualSeat(selectedSeats);
    }

    /**
     * 分配不相邻座位
     *
     * @param numSeats   座位数量
     * @param seatLayout 座位分布
     * @return  分配的座位表
     */
    public static int[][] nonAdjacent(int numSeats, int[][] seatLayout) {
        int numsRows = seatLayout.length;
        int numsCols = seatLayout[0].length;
        List<int[]> selectedSeats = new ArrayList<>();
        for(int i = 0; i < numsRows; i++){
            for(int j = 0; j < numsCols; j++){
                if(seatLayout[i][j] == 0){
                    selectedSeats.add(new int[]{i, j});
                    if(selectedSeats.size() == numSeats){
                        break;
                    }
                }
            }
            if(selectedSeats.size() == numSeats){
                break;
            }
        }
        return convertToActualSeat(selectedSeats);
    }


    private static int[][] convertToActualSeat(List<int[]> selectedSeats) {
        int[][] actualSeat = new int[selectedSeats.size()][2];
        for (int i = 0; i < selectedSeats.size(); i++) {
            int[] seat = selectedSeats.get(i);
            int row = seat[0] + 1;
            int col = seat[1] + 1;
            actualSeat[i][0] = row;
            actualSeat[i][1] = col;
        }
        return actualSeat;
    }
}
