package org.learn.index12306.biz.ticketservice.toolkit;

import lombok.SneakyThrows;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author Milk
 * @version 2023/10/9 20:52
 */
public final class DateUtil {


    /**
     * 计算时间差，返回以 “HH:mm”形式的时间
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 时间差
     */
    public static String calculateHourDifference(Date startTime, Date endTime){
        LocalDateTime startDateTime = startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime endDateTime = endTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        Duration duration = Duration.between(startDateTime, endDateTime);
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        return String.format("%02d:%02d", hours, minutes);
    }


    /**
     * 日期转换为列车行驶开始时间和结束时间
     * 即{@link Date} to {@link String}
     *
     * @param date    时间
     * @param pattern 日期格式
     * @return 日期转换对应的时间
     */
    public static String convertDateToLocalTime(Date date, String pattern){
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(dateTimeFormatter);
    }

    @SneakyThrows
    public static void main(String[] args) {
        String startTime = "2023-08-09 00:59:00";
        String endTime = "2023-08-10 12:44:00";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = simpleDateFormat.parse(startTime);
        Date end = simpleDateFormat.parse(endTime);
        System.out.println(calculateHourDifference(start, end));
    }

}
