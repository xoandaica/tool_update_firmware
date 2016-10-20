package vn.vnpttech.ssdc.nms.mediation.stbacs.executor;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.eclipse.jetty.util.BlockingArrayQueue;
import vn.vnpttech.ssdc.nms.mediation.stbacs.ACSServlet;
import vn.vnpttech.ssdc.nms.mediation.stbacs.common.AppConfig;

/**
 *
 * @author Nguyen Manh Cuong
 */
public final class STBNMSWorker {

    public static class ProcessTR069Task implements Runnable {

        private AsyncContext asyncContext;
        private ACSServlet handler;
        private HttpServletRequest request;
        private HttpServletResponse response;
        private long timerInQueue;

        public ProcessTR069Task(AsyncContext asyncCtx, ACSServlet acsServlet, HttpServletRequest request, HttpServletResponse response) {
            this.asyncContext = asyncCtx;
            this.handler = acsServlet;
            this.request = request;
            this.response = response;
            timerInQueue = System.currentTimeMillis();
        }

        @Override
        public void run() {
            long startTime = System.currentTimeMillis();
//            System.out.println("time in queue: " + (startTime - timerInQueue));
            try {
                handler.processRequest(this.request, this.response); //complete the processing
//                LogManagement.logTimeRunning(STBNMSWorker.class.getName(), "run", "process request", (System.currentTimeMillis() - startTime), LogManagement.TIME_UNIT.MILISECOND);

            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                asyncContext.complete();
            }
        }

    }

    public static int DEFAULT_CORE_POOL_SIZE;
    public static int DEFAULT_MAX_POOL_SIZE;
    ; // equal number of concurent thread of cpu
    public static int DEFAULT_QUEUE_SIZE;
    public static long DEFAULT_TIMEOUT;
    private static STBNMSWorker privateInstance;
    private static final Logger logger = Logger.getLogger(STBNMSWorker.class.getName());

    // index for round robin selected queue
    private static int lastSelectQueue = -1;

    private ExecutorService executorList;
    private BlockingQueue<Runnable> queueList;

    static {

//        numOfWorkerPerQueue = Integer.parseInt(AppParameterConfig.getConfigValueDefault(AppConstant.NO_WORKER_THREAD_PER_QUEUE, DEFAULT_NUM_WORKER_PER_QUEUE));
//        System.out.println(AppConstant.NO_WORKER_THREAD_PER_QUEUE + " : " + numOfWorkerPerQueue);
//
//        numOfQueue = Integer.parseInt(AppParameterConfig.getConfigValueDefault(AppConstant.NO_WORKER_QUEUE, DEFAULT_NUM_QUEUES));
//        System.out.println(AppConstant.NO_WORKER_QUEUE + " : " + numOfQueue);
//        numOfQueue = Integer.valueOf(DEFAULT_NUM_QUEUES);
//        numOfWorkerPerQueue = Integer.valueOf(DEFAULT_NUM_WORKER_PER_QUEUE);
        DEFAULT_CORE_POOL_SIZE = AppConfig.getCorePoolSize();
        DEFAULT_MAX_POOL_SIZE = AppConfig.getMaxPoolSize();
        DEFAULT_QUEUE_SIZE = AppConfig.getQueueSize();
        DEFAULT_TIMEOUT = AppConfig.getQueueTimeOut();

        // khoi tao private instance o thoi diem start chuong trinh
        privateInstance = new STBNMSWorker();
    }

    private STBNMSWorker() {

        // make sure there are no 2 instance of this class
        if (privateInstance != null) {
            throw new IllegalStateException("Already instantiated");
        }
        queueList = new BlockingArrayQueue<Runnable>(DEFAULT_QUEUE_SIZE);
        executorList = new ThreadPoolExecutor(DEFAULT_CORE_POOL_SIZE, DEFAULT_MAX_POOL_SIZE, DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS, queueList);

//        executorList = new ExecutorService[numOfQueue];
//        queueList = new LinkedBlockingDeque[numOfQueue];
//
//        for (int i = 0; i < numOfQueue; i++) { // initial queue
//
//            queueList[i] = new LinkedBlockingDeque(DEFAULT_QUEUE_SIZE);
//            // create the thread pool
//            executorList[i] = new ThreadPoolExecutor(numOfWorkerPerQueue, numOfWorkerPerQueue, 50000L,
//                    TimeUnit.MILLISECONDS, queueList[i]);
//
//        }
    }

    public static STBNMSWorker getInstance() {
        return privateInstance;
    }

    public void processMsg(AsyncContext asyncCtx, ACSServlet acsServlet, HttpServletRequest request, HttpServletResponse response) {
        ProcessTR069Task newTask = new ProcessTR069Task(asyncCtx, acsServlet, request, response);
        this.executorList.submit(newTask);
    }

    /**
     * ham nay goi boi 1 thread cua netty nen ko can thread - safe, neu cac
     * phien ban sau cua netty su dung nhieu threads, thi can phai implement
     * thread safe
     *
     * @param data
     * @return
     */
//    private ExecutorService chooseQueue() {
//
////        lastSelectQueue++;
////        if (lastSelectQueue >= (numOfQueue - 1)) {
////            lastSelectQueue = 0;
////        }
////
////        logger.info("queue selected : " + lastSelectQueue);
////        logger.info("queue size : " + queueList[lastSelectQueue].size());
////
////        return executorList[lastSelectQueue];
//    }
    public static void shutdown() {

        privateInstance.executorList.shutdownNow();
        privateInstance.executorList = null;

        privateInstance = null;
    }

}
