package com.ideaout.im;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class MyApplicationRunner implements ApplicationRunner {


    @Override
    public void run(ApplicationArguments var1) throws Exception {
        System.out.println("MyApplicationRunner class will be execute when the project was started!");
        //runBatchService.startUpdateUserGradeRunBatch();

    }

}