package com.tcl.demo.service;


import com.tcl.demo.entities.User;

public interface UserService
{
    public int addUser(User user);

    public User getUserById(Integer id);
}
