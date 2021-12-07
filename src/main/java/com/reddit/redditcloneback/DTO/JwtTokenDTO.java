package com.reddit.redditcloneback.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtTokenDTO {

    @JsonIgnore
    private String jwtToken;
    private String username;
    private String imageUrl;
}
