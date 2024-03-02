package org.example.lock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ConcurrencyIssueTest {
    private static int count = 0;

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int threadCount = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        ArrayList<Future<Integer>> futures = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            Future<Integer> future = executorService.submit(() -> {
                count = count + 1;
                return count;
            });
            futures.add(future);

        }

        for (Future<Integer> future : futures) {
            future.get();
        }

        System.out.println("count = " + count);
        executorService.shutdown();
    }
}
