package com.bookWise.Impl.userProfile;

import com.bookWise.dao.impl.BookWiseDAOImpl;
import com.bookWise.model.BookWiseUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserProfileRestControllerImpl {

    @Autowired
    private BookWiseDAOImpl bookWiseDAO;

    // In UserProfileRestControllerImpl.java
    public Map<String, Object> getUserProfileInfo(String json) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (json == null || json.isEmpty()) {
                response.put("success", false);
                response.put("message", "Invalid input data");
                return response;
            }

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> userData = mapper.readValue(json, Map.class);

            if (!userData.containsKey("loggedInUserId")) {
                response.put("success", false);
                response.put("message", "User ID is required");
                return response;
            }

            String loggedInUserId = (String) userData.getOrDefault("loggedInUserId", "0");

            BookWiseUser bookWiseUser = (BookWiseUser) bookWiseDAO.find(BookWiseUser.class, Integer.parseInt(loggedInUserId));

            if (bookWiseUser != null) {
                response.put("data", mapper.writeValueAsString(bookWiseUser));
                response.put("success", true);
                response.put("message", "User profile retrieved successfully");
            } else {
                response.put("success", false);
                response.put("message", "User not found");
            }
        } catch (NumberFormatException e) {
            response.put("success", false);
            response.put("message", "Invalid user ID format");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error while retrieving user profile: " + e.getMessage());
        }

        return response;
    }
}
