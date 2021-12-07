package com.reddit.redditcloneback.DTO.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequestUserDTO {

    private String username;
    private String password;
    private String email;
}
