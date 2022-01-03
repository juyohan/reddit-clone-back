package com.reddit.redditcloneback.model.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum LikeType {
    UP_LIKE(1),
    DOWN_LIKE(-1);

    private int direction;

    public static LikeType lookup(Integer direction) {
        return Arrays.stream(LikeType.values())
                .filter(value -> value.getDirection().equals(direction))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Vote not found"));
    }

    public Integer getDirection() {
        return direction;
    }
}
