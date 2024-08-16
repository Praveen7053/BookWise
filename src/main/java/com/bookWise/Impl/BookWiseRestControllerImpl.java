package com.bookWise.Impl;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Component
public class BookWiseRestControllerImpl {

    public  Map<String, Object> saveUpdateNewBooks(HttpServletRequest request){
        Map<String, Object> response = new HashMap<>();
        try {


            System.out.println("called saving");

            response.put("success", true);
            response.put("message", "Books saved or updated successfully.");
        }catch(Exception e){
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error while saving or updating books.");
            return response;
        }
        return response;
    }
}
