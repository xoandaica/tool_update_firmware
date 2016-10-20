/*
 * Copyright 2016 Pivotal Software, Inc..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package vn.vnpttech.ssdc.nms.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import vn.vnpttech.ssdc.nms.webapp.util.ResourceBundleUtils;

/**
 *
 * @author SSDC
 */
public final class ThreadPoolExecutor {

    public static int DEFAULT_CORE_POOL_SIZE;
    public static int DEFAULT_MAX_POOL_SIZE;
    ; // equal number of concurent thread of cpu
    public static int DEFAULT_QUEUE_SIZE;
    public static long DEFAULT_TIMEOUT;
    private static ThreadPoolExecutor privateInstance;
    private static final Logger logger = Logger.getLogger(ThreadPoolExecutor.class.getName());

    // index for round robin selected queue
    private static int lastSelectQueue = -1;

    private ExecutorService executorList;
    private BlockingQueue<Runnable> queueList;

    static {
        DEFAULT_CORE_POOL_SIZE = ResourceBundleUtils.getCorePoolSize();
        DEFAULT_MAX_POOL_SIZE = ResourceBundleUtils.getMaxPoolSize();
        DEFAULT_QUEUE_SIZE = ResourceBundleUtils.getMaxNumberInQueue();
        DEFAULT_TIMEOUT = ResourceBundleUtils.getTimeOutForQueue();
        // khoi tao private instance o thoi diem start chuong trinh
        privateInstance = new ThreadPoolExecutor();
    }

    private ThreadPoolExecutor() {

        // make sure there are no 2 instance of this class
        if (privateInstance != null) {
            throw new IllegalStateException("Already instantiated");
        }
        queueList = new ArrayBlockingQueue<>(DEFAULT_QUEUE_SIZE);
        executorList = new java.util.concurrent.ThreadPoolExecutor(DEFAULT_CORE_POOL_SIZE, DEFAULT_MAX_POOL_SIZE, DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS, queueList);

    }

    public static ThreadPoolExecutor getInstance() {
        if (privateInstance == null) {
            privateInstance = new ThreadPoolExecutor();
        }
        return privateInstance;
    }

    public void processMsg(Runnable task) {
        System.out.println("Size Queue before push" + queueList.size());
        this.executorList.submit(task);
    }

    /**
     * ham nay goi boi 1 thread cua netty nen ko can thread - safe, neu cac
     * phien ban sau cua netty su dung nhieu threads, thi can phai implement
     * thread safe
     *
     * @param data
     * @return
     */
    public static void shutdown() {

        privateInstance.executorList.shutdownNow();
        privateInstance.executorList = null;

        privateInstance = null;
    }

    public void showQueue() {
        System.out.println("Size Queue after push" + queueList.size());
    }
}
