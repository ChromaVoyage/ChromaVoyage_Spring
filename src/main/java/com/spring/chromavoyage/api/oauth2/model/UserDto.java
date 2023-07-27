package com.spring.chromavoyage.api.oauth2.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

public class UserDto {
    @Data
    public static class UserRequest{
        private String email;
        private String picture;
        private String type;//naver or google

        public UserRequest(String email, String picture, String type){
            this.email = email;
            this.picture = picture;
            this.type = type;
        }
    }

    public static class UserResponse{
        private String email;
        private String username;
        private String picture;
        private Timestamp createDate;
        private String provider;//naver or google
        private String accessCode;

        public UserResponse(User user, String accessCode){
            this.email = user.getEmail();
            this.picture = user.getPicture();
            this.createDate = user.getCreateDate();
            this.username = user.getUsername();
            this.provider = user.getProvider();
            this.accessCode = accessCode;
        }
        public UserResponse(User user){
            this.email = user.getEmail();
            this.picture = user.getPicture();
            this.createDate = user.getCreateDate();
            this.username = user.getUsername();
            this.provider = user.getProvider();
        }
    }

}
