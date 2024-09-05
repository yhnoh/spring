package org.example.springmetricexternalapi.retrofit;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
@RequiredArgsConstructor
@Slf4j
public class ExternalApiAspect {


    @Around("execution(* org.example.springmetricexternalapi.retrofit.RetrofitApiService.*(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {


        ExternalApiResponse proceed = null;
        try {
            proceed = (ExternalApiResponse) joinPoint.proceed();
        } finally {
            if (!proceed.isSuccess()) {
                Gson gson = new Gson();
                Object[] args = joinPoint.getArgs();
                ExternalApiRequest externalApiRequest = null;
                for (Object arg : args) {
                    if (arg instanceof ExternalApiRequest) {
                        externalApiRequest = (ExternalApiRequest) arg;
                    }
                }
                log.error("success = " + proceed.isSuccess());
                log.error("externalApiName = " + proceed.getExternalApiName());
                if (externalApiRequest != null) {
                    log.error("request = " + gson.toJson(externalApiRequest));
                }
                log.error("response = " + gson.toJson(proceed));
            }
        }

        return proceed;
    }

}
