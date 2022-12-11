package com.example.springbatchitemreader.error_handler_item_reader.skip_record;

import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;

public class SkipRecordPolicy implements SkipPolicy {
    @Override
    public boolean shouldSkip(Throwable exception, int skipCount) throws SkipLimitExceededException {
        if (exception instanceof Exception && skipCount <= 10) {
            return true;
        } else if (exception instanceof RuntimeException) {
            return false;
        }

        return false;
    }
}
