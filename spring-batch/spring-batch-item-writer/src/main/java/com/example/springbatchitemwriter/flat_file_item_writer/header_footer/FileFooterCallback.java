package com.example.springbatchitemwriter.flat_file_item_writer.header_footer;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.batch.item.file.FlatFileFooterCallback;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * FlatFileFooterCallback는 파일 푸터에 내가 원하는 레코드를 작성할 수 있게 도와준다.
 * Aspect를 통해서 해당 고객의 숫자를 가져왔으면 다른 ItemWriterListener를 활용한 방식도 가능하다.
 */
@Component
@Aspect
public class FileFooterCallback implements FlatFileFooterCallback {

    private int customersSize = 0;

    @Override
    public void writeFooter(Writer writer) throws IOException {
        writer.write("This file contains " + customersSize + " items");
    }

    @Before("execution(* org.springframework.batch.item.support.AbstractFileItemWriter.write(..))")
    public void beforeWrite(JoinPoint joinPoint) {
        List<Customer> customers = (List<Customer>) joinPoint.getArgs()[0];

        this.customersSize += customers.size();
    }
}
