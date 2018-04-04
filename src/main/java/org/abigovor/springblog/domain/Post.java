package org.abigovor.springblog.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Post {
    @Id @GeneratedValue private Integer id;
    private String title;
    private String body;
    private Date createdAt;
    private Date updatedAt;

    @ManyToOne private User user;
}
