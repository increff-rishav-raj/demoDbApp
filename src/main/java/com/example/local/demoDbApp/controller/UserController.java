package com.example.local.demoDbApp.controller;

//import com.example.local.demoDbApp.api.UserApi;

import com.example.local.demoDbApp.model.UserData;
import com.example.local.demoDbApp.model.UserForm;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(value = "/admin")
@Log4j2
public class UserController {


//    @Autowired
//    private UserApi api;

    @Operation(summary = "Add user", description = "add an user with his password to database")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public UserData addUser(@RequestBody UserForm form) throws IllegalAccessException {
        return new UserData();
    }

    @Operation(summary = "Add user", description = "get all users")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public List<UserData> getAllUser() throws IllegalAccessException {
        return Collections.singletonList(new UserData());
    }

    @Operation(summary = "Print Log", description = "Print Logs")
    @RequestMapping(value = "/log", method = RequestMethod.GET)
    public void getSecret() {
        log.info("Info");
        log.debug("Debug");
        log.error("Error");
        log.warn("Warn");
        log.fatal("Fatal");
        log.trace("Trace");
    }

}
