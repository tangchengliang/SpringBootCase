package com.tcl.demo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.tcl.demo.mapper") //import tk.mybatis.spring.annotation.MapperScan;
public class Main24618 {
    public static void main(String[] args) {

        SpringApplication.run(Main24618.class, args);

    }
}