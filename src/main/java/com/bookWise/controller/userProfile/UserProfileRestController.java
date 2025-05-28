package com.bookWise.controller.userProfile;

import com.bookWise.Impl.userProfile.UserProfileRestControllerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping(value = "/api/user/profile")
public class UserProfileRestController {

    @Autowired
    private UserProfileRestControllerImpl userProfileRestControllerImpl;

    @RequestMapping(method = RequestMethod.POST, value = "/getUserProfileInfo")
    public Map<String, Object> getUserProfileInfo(@RequestBody String json) {
        return userProfileRestControllerImpl.getUserProfileInfo(json);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/saveUserProfileInfo")
    public Map<String, Object> saveUserProfileInfo(@RequestBody String json) {
        return userProfileRestControllerImpl.saveUserProfileInfo(json);
    }
}
