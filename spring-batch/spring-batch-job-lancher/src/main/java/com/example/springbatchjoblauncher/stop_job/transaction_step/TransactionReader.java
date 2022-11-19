package com.example.springbatchjoblauncher.stop_job.transaction_step;

import com.example.springbatchjoblauncher.stop_job.Transaction;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.item.*;
import org.springframework.batch.item.file.transform.FieldSet;

public class TransactionReader implements ItemStreamReader<Transaction> {

    private ItemStreamReader<FieldSet> fieldSetReader;
    private int recordCount = 0;
    private int expectedRecodeCount = 0;

    public TransactionReader(ItemStreamReader<FieldSet> fieldSetReader) {
        this.fieldSetReader = fieldSetReader;
    }

    @Override
    public Transaction read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return process(fieldSetReader.read());
    }

    private Transaction process(FieldSet fieldSet){
        Transaction result = null;

        if(fieldSet != null){
            if(fieldSet.getFieldCount() > 1){
                result = new Transaction();
                result.setAccountNumber(fieldSet.readString(0));
                result.setTimestamp(fieldSet.readDate(1, "yyyy-MM-dd HH:mm:ss"));
                result.setAmount(fieldSet.readDouble(2));

                recordCount++;
            }else{
                expectedRecodeCount = fieldSet.readInt(0);
            }
        }
        return result;
    }

    public void setFieldSetReader(ItemStreamReader<FieldSet> fieldSetReader){
        this.fieldSetReader = fieldSetReader;
    }

    @AfterStep
    public ExitStatus afterStep(StepExecution execution){
        if(recordCount == expectedRecodeCount){
            return execution.getExitStatus();
        }else{
            return ExitStatus.STOPPED;
        }
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        this.fieldSetReader.open(executionContext);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        this.fieldSetReader.update(executionContext);
    }

    @Override
    public void close() throws ItemStreamException {
        this.fieldSetReader.close();
    }
}
