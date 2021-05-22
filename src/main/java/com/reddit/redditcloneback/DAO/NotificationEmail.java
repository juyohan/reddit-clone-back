package com.reddit.redditcloneback.DAO;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEmail {
    private String subject;
    private String recipient;
    private String body;
}
