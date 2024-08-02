package org.example.springbatchjoblauncher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.example.springbatchjoblauncher.v1")
//@SpringBootApplication(scanBasePackages = "org.example.springbatchjoblauncher.v2")
//@SpringBootApplication(scanBasePackages = "org.example.springbatchjoblauncher.v3")
public class SpringBatchJobLauncherApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchJobLauncherApplication.class, args);
    }

}
