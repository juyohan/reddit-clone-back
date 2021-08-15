package com.reddit.redditcloneback.DAO;

import com.reddit.redditcloneback.Error.SpringRedditException;

import java.util.Arrays;

public enum LikeType {
    UPLIKE(1), DOWNLIKE(-1);

    private int direction;

    LikeType(int direction) {
    }

    public static LikeType lookup(Integer direction) {
        return Arrays.stream(LikeType.values())
                .filter(value -> value.getDirection().equals(direction))
                .findAny()
                .orElseThrow(() -> new SpringRedditException("Vote not found"));
    }

    public Integer getDirection() {
        return direction;
    }
}
