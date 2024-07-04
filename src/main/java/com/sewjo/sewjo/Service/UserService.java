package com.sewjo.sewjo.Service;

import com.sewjo.sewjo.Dto.LoginDTO;
import com.sewjo.sewjo.Dto.UserDTO;
import com.sewjo.sewjo.response.LoginResponse;

public interface UserService {

    String addUser(UserDTO userDTO);

    LoginResponse loginUser(LoginDTO loginDTO);

}
