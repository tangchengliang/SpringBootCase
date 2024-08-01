package com.tcl.demo.controller;

import com.tcl.demo.entities.User;
import com.tcl.demo.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping(value = "/user/add")
    public int addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @GetMapping(value = "/user/{id}")
    public User getUserById(@PathVariable("id") Integer id) {
        return userService.getUserById(id);
    }


}



/*

###
POST http://localhost:24618/user/add
Content-Type: application/json

{
  "username": "尚硅谷05",
  "password": "13911111115",
  "sex": "1"
}
*/
