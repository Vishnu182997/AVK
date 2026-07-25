package com.example.appointment;
import org.springframework.boot.SpringApplication; import org.springframework.boot.autoconfigure.SpringBootApplication; import org.springframework.scheduling.annotation.EnableScheduling;
@SpringBootApplication @EnableScheduling public class AppointmentQueueApplication { public static void main(String[] args){SpringApplication.run(AppointmentQueueApplication.class,args);} }
