package org.example.springredissionlock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RequestLockServiceTest {

    @Autowired
    private RequestLockService requestLockService;


    @Test
    void noLockTest() {
        //given
        int n = 11;
        //when
        for(int i = 0; i < n; i++) {
            requestLockService.noLock();
        }
        //then
    }


    @Test
    void noLockWhenConcurrencyTest() throws InterruptedException {

        int nThreads = 11;
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        CountDownLatch latch = new CountDownLatch(nThreads);

        for(int i = 0; i < nThreads; i++) {
            executorService.submit(() -> {
                try {
                    requestLockService.noLock();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();
    }


    @Test
    void lettuceLockTest() throws InterruptedException {
        int nThreads = 11;
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        CountDownLatch latch = new CountDownLatch(nThreads);

        for(int i = 0; i < nThreads; i++) {
            executorService.submit(() -> {
                try {
                    requestLockService.lettuceLock("lettuceLock");
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();
    }


    @Test
    void redissonLockTest() throws InterruptedException {

        int nThreads = 11;
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        CountDownLatch latch = new CountDownLatch(nThreads);

        for(int i = 0; i < nThreads; i++) {
            executorService.submit(() -> {
                try {
                    requestLockService.redissonLock("redissonLock");
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();
    }
}