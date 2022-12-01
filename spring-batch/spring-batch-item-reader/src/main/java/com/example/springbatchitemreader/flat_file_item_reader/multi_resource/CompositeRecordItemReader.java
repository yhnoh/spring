package com.example.springbatchitemreader.flat_file_item_reader.multi_resource;

import org.springframework.batch.item.*;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.core.io.Resource;

public class CompositeRecordItemReader implements ResourceAwareItemReaderItemStream<Customer> {

    private Object curItem = null;
    private ResourceAwareItemReaderItemStream<Object> delegate;

    public CompositeRecordItemReader(ResourceAwareItemReaderItemStream<Object> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void setResource(Resource resource) {
        delegate.setResource(resource);
    }

    @Override
    public Customer read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if(curItem == null){
            curItem = delegate.read();
        }

        Customer item = (Customer) curItem;
        curItem = null;
        /**
         * 제어 중지 로직
         * 고객 정보를 담은 이후 고객 정보에 거래 내역 정보를 담고 있다.
         */
        if(item != null){
            while (this.peek() instanceof Transaction){
                item.addTransaction((Transaction) curItem);
                curItem = null;
            }
        }
        return item;
    }

    private Object peek() throws Exception {
        if(curItem == null){
            curItem = delegate.read();
        }
        return curItem;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        delegate.open(executionContext);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        delegate.update(executionContext);
    }

    @Override
    public void close() throws ItemStreamException {
        delegate.close();
    }


}
