package com.bookWise.controller.userHome;

import com.bookWise.Impl.userHome.UserHomeRestControllerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/api/book/userHome/actions")
public class UserHomeRestController {

    @Autowired
    private UserHomeRestControllerImpl userHomeRestController;

    @RequestMapping(method = RequestMethod.POST, value = "/getAllBooksUser")
    public ResponseEntity<Map<String, Object>> getAllBooksUser(@RequestBody String json) {
        return userHomeRestController.loadAllBooks(json);
    }

}
