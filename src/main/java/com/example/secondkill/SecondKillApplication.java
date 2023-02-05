package com.example.secondkill;

import com.example.secondkill.main.service.FilterService;
import com.example.secondkill.main.service.PromoItemService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class SecondKillApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run=SpringApplication.run(SecondKillApplication.class, args);
        FilterService filterService=(FilterService) run.getBean("FilterService");
        filterService.load();
        PromoItemService promoItemService=(PromoItemService) run.getBean("PromoItemService");
        promoItemService.load();
    }

}
