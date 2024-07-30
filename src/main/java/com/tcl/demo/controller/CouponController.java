package com.tcl.demo.controller;


import com.tcl.demo.service.CouponService;
import com.tcl.demo.service.CouponServiceV2;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther zzyy
 * @create 2024-05-08 17:04
 */

@RestController
@Slf4j
public class CouponController
{
    @Resource
    private CouponService couponService;

    //http://localhost:24618/coupon/sendv1
    @GetMapping(value = "/coupon/sendv1")
    public void sendv1()
    {
        couponService.batchTaskAction();
    }

    @Resource
    private CouponServiceV2 couponServiceV2;
    //http://localhost:24618/coupon/sendv2
    @GetMapping(value = "/coupon/sendv2")
    public void sendv2()
    {
        couponServiceV2.batchTaskActionV2();
    }
}
