package org.learn.index12306.framework.starter.common.toolkit;

import lombok.SneakyThrows;

/**
 * 线程池工具类
 *
 * @author Milk
 * @version 2023/9/30 17:35
 */
public final class ThreadUtil {

    /**
     * 睡眠当前线程指定时间 {@param millis}
     *
     * @param millis 睡眠时间，单位毫秒
     */
    @SneakyThrows(value = InterruptedException.class)
    public static void sleep(long millis) {
        Thread.sleep(millis);
    }
}
