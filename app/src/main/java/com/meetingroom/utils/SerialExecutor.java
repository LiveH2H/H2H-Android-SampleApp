package com.meetingroom.utils;

import java.util.ArrayDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class SerialExecutor {
    private Runnable mActive;
    private ArrayDeque<Runnable> mArrayDeque = new ArrayDeque<>();

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE = 1;
    private static final BlockingQueue<Runnable> sPoolWorkQueue =
            new LinkedBlockingDeque<>(128);
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "Serial thread #" + mCount.getAndIncrement());
        }
    };
    private static final ThreadPoolExecutor THREAD_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE,
            MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);

    public synchronized void execute(final Runnable r) {
        mArrayDeque.offer(new Runnable() {
            @Override
            public void run() {
                try {
                    r.run();
                } finally {
                    scheduleNext();
                }
            }
        });
        // 第一次入队列时mActivie为空，因此需要手动调用scheduleNext方法
        if (mActive == null) {
            scheduleNext();
        }
    }

    private void scheduleNext() {
        if ((mActive = mArrayDeque.poll()) != null) {
            THREAD_EXECUTOR.execute(mActive);
        }
    }

    public static void main(String[] args) {
        SerialExecutor serialExecutor = new SerialExecutor();
        for (int i = 0; i < 10; i ++) {
            final int j = i;
            serialExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("The num is :" + (j + 1));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}