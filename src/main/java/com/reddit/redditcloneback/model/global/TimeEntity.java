package com.reddit.redditcloneback.model.global;

import lombok.Getter;

import javax.persistence.*;
import java.time.ZonedDateTime;

@MappedSuperclass
@Getter
public abstract class TimeEntity {

    private ZonedDateTime createdDate;
    private ZonedDateTime modifiedDate;

    @PrePersist
    public void prePersist() {
        this.createdDate = ZonedDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.modifiedDate = ZonedDateTime.now();
    }
}
