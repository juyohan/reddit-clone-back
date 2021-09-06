package com.reddit.redditcloneback.DTO;

import com.reddit.redditcloneback.DAO.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtTokenDTO {

    private String jwtToken;

    private String username;
}
