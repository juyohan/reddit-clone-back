package com.reddit.redditcloneback.DAO;

import lombok.Data;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
public abstract class BasicEntity {

    private LocalDateTime createDate;
    private String createBy;
    private LocalDateTime modifiedDate;
    private String modifiedBy;
}
