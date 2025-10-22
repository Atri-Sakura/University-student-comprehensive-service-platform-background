package com.ruoyi.platform.chat.utils;

import java.util.concurrent.TimeUnit;

/**
 * 雪花算法工具类（生成64位唯一ID）
 * 结构：0(符号位) + 41(时间戳) + 5(数据中心ID) + 5(工作节点ID) + 12(序列号)
 * 可支持69年不重复，每个节点每毫秒最多生成4096个ID
 */
public class SnowflakeIdGenerator {

    /** 开始时间戳（UTC 2020-01-01 00:00:00，可自行调整） */
    private static final long EPOCH = 1577836800000L;

    /** 数据中心ID所占位数（5位，支持0-31共32个数据中心） */
    private static final long DATA_CENTER_ID_BITS = 5L;

    /** 工作节点ID所占位数（5位，支持0-31共32个节点） */
    private static final long WORKER_ID_BITS = 5L;

    /** 序列号所占位数（12位，每个节点每毫秒最多生成4096个ID） */
    private static final long SEQUENCE_BITS = 12L;

    /** 数据中心ID的最大值（2^5 - 1 = 31） */
    private static final long MAX_DATA_CENTER_ID = ~(-1L << DATA_CENTER_ID_BITS);

    /** 工作节点ID的最大值（2^5 - 1 = 31） */
    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);

    /** 序列号的最大值（2^12 - 1 = 4095） */
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);

    /** 工作节点ID左移位数（12位） */
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;

    /** 数据中心ID左移位数（12 + 5 = 17位） */
    private static final long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

    /** 时间戳左移位数（12 + 5 + 5 = 22位） */
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS;

    /** 数据中心ID（配置时确保唯一） */
    private final long dataCenterId;

    /** 工作节点ID（配置时确保唯一） */
    private final long workerId;

    /** 序列号（同一毫秒内自增） */
    private long sequence = 0L;

    /** 上次生成ID的时间戳（毫秒） */
    private long lastTimestamp = -1L;

    /**
     * 构造方法（指定数据中心ID和工作节点ID）
     * @param dataCenterId 数据中心ID（0-31）
     * @param workerId 工作节点ID（0-31）
     * @throws IllegalArgumentException 当ID超出范围时抛出异常
     */
    public SnowflakeIdGenerator(long dataCenterId, long workerId) {
        if (dataCenterId < 0 || dataCenterId > MAX_DATA_CENTER_ID) {
            throw new IllegalArgumentException("数据中心ID必须在0-" + MAX_DATA_CENTER_ID + "之间");
        }
        if (workerId < 0 || workerId > MAX_WORKER_ID) {
            throw new IllegalArgumentException("工作节点ID必须在0-" + MAX_WORKER_ID + "之间");
        }
        this.dataCenterId = dataCenterId;
        this.workerId = workerId;
    }

    /**
     * 生成下一个唯一ID
     * @return 64位Long类型唯一ID
     */
    public synchronized long nextId() {
        long timestamp = System.currentTimeMillis();

        // 处理时钟回拨（如果当前时间小于上次生成ID的时间）
        if (timestamp < lastTimestamp) {
            long offset = lastTimestamp - timestamp;
            // 允许5毫秒内的回拨（避免服务器时钟微调导致失败）
            if (offset <= 5) {
                try {
                    TimeUnit.MILLISECONDS.sleep(offset + 1);
                    timestamp = System.currentTimeMillis();
                } catch (InterruptedException e) {
                    throw new RuntimeException("时钟回拨处理失败", e);
                }
            } else {
                // 超过5毫秒的回拨视为异常，防止ID重复
                throw new RuntimeException("时钟回拨异常，拒绝生成ID，时间差：" + offset + "毫秒");
            }
        }

        // 同一毫秒内，序列号自增
        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            // 序列号溢出（超过4095），等待下一毫秒
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            // 不同毫秒，序列号重置为0
            sequence = 0L;
        }

        // 更新上次生成ID的时间戳
        lastTimestamp = timestamp;

        // 组合ID：时间戳差 + 数据中心ID + 工作节点ID + 序列号
        return ((timestamp - EPOCH) << TIMESTAMP_SHIFT)
                | (dataCenterId << DATA_CENTER_ID_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence;
    }

    /**
     * 等待到下一毫秒（解决序列号溢出问题）
     * @param lastTimestamp 上次生成ID的时间戳
     * @return 新的时间戳
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

    /**
     * 单例模式（默认数据中心ID=0，工作节点ID=0，适用于单机环境）
     */
    private static class SingletonHolder {
        // 单机环境默认节点配置，分布式环境需手动指定dataCenterId和workerId
        private static final SnowflakeIdGenerator INSTANCE = new SnowflakeIdGenerator(0, 0);
    }

    /**
     * 获取单例实例（单机环境直接使用）
     * @return 雪花算法实例
     */
    public static SnowflakeIdGenerator getInstance() {
        return SingletonHolder.INSTANCE;
    }

    // 测试方法（运行后可查看生成的ID）
    public static void main(String[] args) {
        // 单机环境测试
        SnowflakeIdGenerator generator = SnowflakeIdGenerator.getInstance();
        for (int i = 0; i < 10; i++) {
            System.out.println("生成ID：" + generator.nextId());
        }

        // 多线程测试（验证线程安全）
        /*
        SnowflakeIdGenerator multiThreadGenerator = new SnowflakeIdGenerator(1, 1);
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                for (int j = 0; j < 5; j++) {
                    System.out.println("线程" + Thread.currentThread().getId() + "生成ID：" + multiThreadGenerator.nextId());
                }
            }).start();
        }
        */
    }
}