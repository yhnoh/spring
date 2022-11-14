package com.example.springbatchjob.chunk.custom_chunk_size;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.Random;

@Slf4j
public class RandomCompletionPolicy implements CompletionPolicy {

    private int chunkSize = 0;
    private int totalProcessed = 0;
    private Random random = new Random();

    /**
     * 정크 완료 여부의 상태를 기반으로 로직 수행
     */
    @Override
    public boolean isComplete(RepeatContext context, RepeatStatus result) {
        return RepeatStatus.FINISHED == result ? true : isComplete(context);
    }

    /**
     * 장크 완료 여부를 판단
     */
    @Override
    public boolean isComplete(RepeatContext context) {
        return this.totalProcessed >= chunkSize;
    }

    @Override
    public RepeatContext start(RepeatContext parent) {
        this.chunkSize = random.nextInt(20);
        this.totalProcessed = 0;

        log.info("chunk size has bean set to {}", this.chunkSize);
        return parent;
    }

    @Override
    public void update(RepeatContext context) {
        this.totalProcessed++;
    }
}
