package com.example.springbatchjoblauncher.stop_job.step_execution_stop;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.*;
import org.springframework.batch.item.file.transform.FieldSet;

public class StepExecutionStopItemReader implements ItemStreamReader<StepExecutionTransaction> {

    private ItemStreamReader<FieldSet> fieldSetReader;
    private int recordCount = 0;
    private StepExecution execution;
    private int expectedRecodeCount = 0;

    public StepExecutionStopItemReader(ItemStreamReader<FieldSet> fieldSetReader, Integer expectedRecodeCount) {
        this.fieldSetReader = fieldSetReader;
        this.expectedRecodeCount = expectedRecodeCount;
    }

    @Override
    public StepExecutionTransaction read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return process(fieldSetReader.read());
    }

    private StepExecutionTransaction process(FieldSet fieldSet){
        StepExecutionTransaction result = null;

        if(fieldSet != null){
            if(fieldSet.getFieldCount() > 1){
                result = new StepExecutionTransaction();
                result.setAccountNumber(fieldSet.readString(0));
                result.setTimestamp(fieldSet.readDate(1, "yyyy-MM-dd HH:mm:ss"));
                result.setAmount(fieldSet.readDouble(2));

                recordCount++;
            }else {
                if(expectedRecodeCount != this.recordCount){
                    this.execution.setTerminateOnly();
                }
            }
        }
        return result;
    }


    @BeforeStep
    public void beforeSte(StepExecution execution){
        this.execution = execution;
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
