package com.example.local.demoDbApp.controller;

import com.example.local.demoDbApp.api.UserApi;
import com.example.local.demoDbApp.model.UserData;
import com.example.local.demoDbApp.model.UserForm;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/admin")
public class UserController {


    @Autowired
    private UserApi api;

    @Operation(summary = "Add user", description = "add an user with his password to database")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public UserData addUser(@RequestBody UserForm form) throws IllegalAccessException {
        return api.addUser(form);
    }

    @Operation(summary = "Add user", description = "get all users")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public List<UserData> getAllUser() throws IllegalAccessException {
        return api.getAll();
    }

    @Operation(summary = "Get secret", description = "Get secrets")
    @RequestMapping(value = "/secret", method = RequestMethod.GET)
    public Map<String, String> getSecret() throws IllegalAccessException {
        Map<String,String> map = new HashMap<>();
        return map;
    }

}
