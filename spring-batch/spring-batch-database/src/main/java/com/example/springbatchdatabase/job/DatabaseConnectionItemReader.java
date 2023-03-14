package com.example.springbatchdatabase.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.support.AbstractItemStreamItemReader;

@RequiredArgsConstructor
public class DatabaseConnectionItemReader<T> extends AbstractItemStreamItemReader<T> {

    private final AbstractItemStreamItemReader<T> itemStreamItemReader;

    @Override
    public T read() throws Exception {
        T item = itemStreamItemReader.read();
//        Thread.sleep(1000);
        return item;
    }

    @Override
    public void close() {
        itemStreamItemReader.close();
    }

    @Override
    public void open(ExecutionContext executionContext) {
        itemStreamItemReader.open(executionContext);
    }

    @Override
    public void update(ExecutionContext executionContext) {
        itemStreamItemReader.update(executionContext);
    }
}
