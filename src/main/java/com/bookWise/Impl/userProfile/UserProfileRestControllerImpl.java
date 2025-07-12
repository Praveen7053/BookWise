package com.bookWise.Impl.userProfile;

import com.bookWise.SecurityConfig.loginUserConfig.BookWiseLoginUser;
import com.bookWise.common.dto.ImageResponse;
import com.bookWise.common.dto.UserProfileDTO;
import com.bookWise.dao.impl.BookWiseDAOImpl;
import com.bookWise.model.BookWiseUser;
import com.bookWise.util.FileUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
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
            if (bookWiseUser != null && StringUtils.isNotBlank(bookWiseUser.getProfilePicturePath())) {
                ImageResponse imageResponse = FileUtils.getImageContentAndMimeType(bookWiseUser.getProfilePicturePath(),"UserProfile");

                if (imageResponse != null) {
                    response.put("imageContent", imageResponse.getImageContent());
                    response.put("imageMimeType", imageResponse.getImageMimeType());
                }
            }

            UserProfileDTO userProfileData = new UserProfileDTO(bookWiseUser);
            if (bookWiseUser != null) {
                response.put("data", mapper.writeValueAsString(userProfileData));
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

    @Transactional
    public Map<String, Object> saveUserProfileInfo(String json) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (StringUtils.isBlank(json)) {
                response.put("success", false);
                response.put("message", "Invalid input data");
                return response;
            }

            // Get authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            BookWiseLoginUser currentUser = (BookWiseLoginUser) authentication.getPrincipal();
            Integer loggedInUserId = currentUser.getUserId();

            // Parse the JSON input
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> userData = mapper.readValue(json, Map.class);

            // Fetch existing user
            BookWiseUser existingUser = (BookWiseUser) bookWiseDAO.find(BookWiseUser.class, loggedInUserId);
            if (existingUser == null) {
                response.put("success", false);
                response.put("message", "User not found");
                return response;
            }

            // Handle profile image if present
            String profileImage = (String) userData.get("profileImage");
            if (StringUtils.isNotBlank(profileImage)) {
                try {
                    // Save the image using FileUtils
                    String mainDir = "profiles";
                    String uniqueSuffix = loggedInUserId.toString();
                    String relativePath = FileUtils.saveBase64ToFile(profileImage, "UserProfile", "user_" + existingUser.getUserName(), uniqueSuffix);

                    // Delete old profile image if exists
                    if (StringUtils.isNotBlank(existingUser.getProfilePicturePath())) {
                        FileUtils.deleteFolder(existingUser.getProfilePicturePath());
                    }

                    // Update user's profile image path
                    existingUser.setProfilePicturePath(relativePath);
                } catch (Exception e) {
                    e.printStackTrace();
                    response.put("success", false);
                    response.put("message", "Error saving profile image: " + e.getMessage());
                    return response;
                }
            }

            // Update user information
            if (userData.get("userName") != null) {
                existingUser.setUserName((String) userData.get("userName"));
            }
            if (userData.get("userEmail") != null) {
                existingUser.setUserEmail((String) userData.get("userEmail"));
            }
            if (userData.get("userPhoneNumber") != null) {
                existingUser.setUserPhoneNumber((String) userData.get("userPhoneNumber"));
            }
            if (userData.get("age") != null && !String.valueOf(userData.get("age")).equals("null")) {
                try {
                    existingUser.setAge(Integer.parseInt(String.valueOf(userData.get("age"))));
                } catch (NumberFormatException ignored) {}
            }
            if (userData.get("gender") != null) {
                existingUser.setGender((String) userData.get("gender"));
            }
            if (userData.get("mainLanguage") != null) {
                existingUser.setMainLanguage((String) userData.get("mainLanguage"));
            }

            if (userData.get("street") != null) {
                existingUser.setStreet((String) userData.get("street"));
            }

            if (userData.get("city") != null) {
                existingUser.setCity((String) userData.get("city"));
            }

            if (userData.get("state") != null) {
                existingUser.setState((String) userData.get("state"));
            }

            if (userData.get("zip") != null) {
                existingUser.setZipCode((String) userData.get("zip"));
            }

            if (userData.get("user_description") != null) {
                existingUser.setDescription((String) userData.get("user_description"));
            }

            // Update modification timestamp and modifier
            existingUser.setModifiedDate(new Timestamp(new Date().getTime()));
            existingUser.setModifiedBy(loggedInUserId.toString());

            // Save the updated user
            bookWiseDAO.saveOrUpdate(existingUser);

            response.put("success", true);
            response.put("message", "Profile updated successfully");
            response.put("data", mapper.writeValueAsString(existingUser));

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error updating profile: " + e.getMessage());
        }
        return response;
    }
}
