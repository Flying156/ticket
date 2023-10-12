package org.learn.index12306.biz.ticketservice.common.constant;

/**
 * Redis Key 定义常量类
 *
 * @author Milk
 * @version 2023/10/7 18:48
 */
public final class RedisKeyConstant {

    /**
     * 列车基本信息，Key Prefix + 列车ID
     */
    public static final String TRAIN_INFO = "index12306-ticket-service:train_info:";

    /**
     * 地区与站点映射查询
     */
    public static final String REGION_TRAIN_STATION_MAPPING = "index12306-ticket-service:region_train_station_mapping";

    /**
     * 地区与站点映射查询分布式锁 Key
     */
    public static final String LOCK_REGION_TRAIN_STATION_MAPPING = "index12306-ticket-service:lock:region_train_station_mapping";

    /**
     * 站点查询，Key Prefix + 起始城市_终点城市_日期
     */
    public static final String REGION_TRAIN_STATION = "index12306-ticket-service:region_train_station:%s_%s";

    /**
     * 站点查询分布式锁 Key
     */
    public static final String LOCK_REGION_TRAIN_STATION = "index12306-ticket-service:lock:region_train_station";


    /**
     * 列车站点座位价格查询，Key Prefix + 列车ID_起始城市_终点城市
     */
    public static final String TRAIN_STATION_PRICE = "index12306-ticket-service:train_station_price:%s_%s_%s";

    /**
     * 站点余票查询，Key Prefix + 列车ID_起始站点_终点
     */
    public static final String TRAIN_STATION_REMAINING_TICKET = "index12306-ticket-service:train_station_remaining_ticket:";

    /**
     * 地区以及车站查询，Key Prefix + ( 车站名称 or 查询方式 )
     */
    public static final String REGION_STATION = "index12306-ticket-service:region-station:";

    /**
     * 列车路线信息查询，Key Prefix + 列车ID
     */
    public static final String TRAIN_STATION_STOPOVER_DETAIL = "index12306-ticket-service:train_station_stopover_detail:";

    /**
     * 列车站点缓存
     */
    public static final String STATION_ALL = "index12306-ticket-service:all_station:";

    /**
     * 获取地区以及站点集合分布式锁 Key
     */
    public static final String LOCK_QUERY_REGION_STATION_LIST = "index12306-ticket-service:lock:query_region_station_list_%s";

    /**
     * 获取全部地点集合 Key
     */
    public static final String QUERY_ALL_REGION_LIST = "index12306-ticket-service::query_all_region_list";

    /**
     * 获取所有地点集合分布式锁 key
     */
    public static final String LOCK_QUERY_ALL_REGION_LIST = "index12306-ticket-service:lock:query_all_region_list";


    /**
     * 获取相邻座位余票分布式锁 Key
     */
    public static final String LOCK_SAFE_LOAD_SEAT_MARGIN_GET = "index12306-ticket-service:lock:safe_load_seat_margin_%s";
}
