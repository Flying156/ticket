package org.learn.index12306.biz.ticketservice.toolkit;

import org.learn.index12306.biz.ticketservice.dto.domain.TicketListDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

/**
 * 自定义时间比较器
 *
 * @author Milk
 * @version 2023/10/9 22:13
 */
public class TimeStringComparator implements Comparator<TicketListDTO> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public int compare(TicketListDTO ticketList1, TicketListDTO ticketList2) {
        LocalDateTime time1 = LocalDateTime.parse(ticketList1.getDepartureTime(), FORMATTER);
        LocalDateTime time2 = LocalDateTime.parse(ticketList2.getDepartureTime(), FORMATTER);
        return time1.compareTo(time2);
    }
}
