package com.tcl.demo.utils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.function.Consumer;


public class TaskBatchSendUtils
{
    public static <T> void send(List<T> taskList, Executor threadPool, Consumer<? super T> consumer) throws InterruptedException
    {
        if (taskList == null || taskList.isEmpty())
        {
            return;
        }

        if(Objects.isNull(consumer))
        {
            return;
        }

        CountDownLatch countDownLatch = new CountDownLatch(taskList.size());

        for (T couponOrShortMsg : taskList)
        {
            threadPool.execute(() ->
            {
                try
                {
                    consumer.accept(couponOrShortMsg);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
    }

    public static void disposeTask(String task)
    {
        System.out.println(String.format("【%s】disposeTask下发优惠卷或短信成功", task));
    }

    public static void disposeTaskV2(String task)
    {
        System.out.println(String.format("【%s】disposeTask下发邮件成功", task));
    }

    public static void disposeTaskV3(String task)
    {
        System.out.println(String.format("【%s】disposeTask下发二维码序号成功", task));
    }

}
