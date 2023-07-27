package com.spring.chromavoyage.api.oauth2.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Builder
@Getter
@Entity(name = "USER")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;
    @Column(name = "name")
    private String username;
    private String email;
    //private String role; //ROLE_USER, ROLE_ADMIN

    private String provider;
    private String picture;
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Column(name = "create_date")
    private Timestamp createDate;

    public void updateNmAndPicture(String username, String picture){
        this.username = username;
        this.picture = picture;
    }
    public void updateProvider(String provider){
        this.provider = provider;
    }
}
