package com.pda.hm_texas.helper;

import com.pda.hm_texas.dto.UserDTO;

public class LoginUser {

    private UserDTO User;
    private static LoginUser Instance = null;

    public UserDTO getUser() {
        return User;
    }

    public void setUser(UserDTO user) {
        User = user;
    }

    public static synchronized LoginUser getInstance(){
        if(Instance == null)
        {
            Instance = new LoginUser();
        }

        return Instance;
    }
}
