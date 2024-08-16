package com.bookWise.controller;

import com.bookWise.Impl.BookWiseRestControllerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.util.Map;


@RestController
@RequestMapping(value = "/api/book/actions")
public class BookWiseRestController {

    @Autowired
    private BookWiseRestControllerImpl bookWiseRestControllerImpl;

    @RequestMapping(method = RequestMethod.POST, value = "/saveUpdateNewBooks")
    public Map<String, Object> saveUpdateNewBooks(@RequestBody String bookDataJson) {
        return bookWiseRestControllerImpl.saveUpdateNewBooks(bookDataJson);
    }

}
