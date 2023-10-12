package org.learn.index12306.biz.ticketservice.toolkit;

import org.learn.index12306.biz.ticketservice.dto.domain.RouteDTO;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @author Milk
 * @version 2023/10/10 17:13
 */
public final class StationCalculateUtil {

    /**
     * 计算出发站和终点站中间的站点（包含出发站和终点站）
     *
     * @param stations     所有站点数据
     * @param startStation 出发站
     * @param endStation   终点站
     * @return 出发站和终点站中间的站点（包含出发站和终点站）
     */
    public static List<RouteDTO> throughStation(List<String> stations, String startStation, String endStation){
        List<RouteDTO> routesToDeduct = new ArrayList<>();
        int startIndex = stations.indexOf(startStation);
        int endIndex = stations.indexOf(endStation);
        if(startIndex < 0 || endIndex < 0 || startIndex >= endIndex){
            return routesToDeduct;
        }
        for(int i = startIndex; i < endIndex; i++){
            for(int j = i + 1; j <= endIndex; j++){
                routesToDeduct.add(new RouteDTO(stations.get(i), stations.get(j)));
            }
        }
        return routesToDeduct;
    }

}
