package com.example.springbatchstock.stock.daily;

import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.Map;

public class DailyStockDataJobParametersValidator implements JobParametersValidator {

    @Override
    public void validate(JobParameters jobParameters) throws JobParametersInvalidException {
        Map<String, JobParameter> parameters = jobParameters.getParameters();
        if (parameters.get("dailyStockFile") == null) {
            throw new JobParametersInvalidException("dailyStockFile parameter is not found");
        }

        String dailyStockFile = (String) parameters.get("dailyStockFile").getValue();
        Resource resource = new ClassPathResource(dailyStockFile);
        if (!resource.exists()) {
            throw new JobParametersInvalidException("dailyStockFile resource is not found");
        }
    }
}
