package com.wallet.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String email;
    private String userId;
    private String username;
    private int memberType;
    private int status;
    private String remark;

    @OneToOne
    @JoinColumn(name = "sub_user_api_key_id", referencedColumnName = "id")
    private SubUserApiKey subUserApiKey;


    public SubUser(String userId,String username,int memberType,int status,String remark,String email)
    {
        this.userId=userId;
        this.username=username;
        this.memberType=memberType;
        this.status=status;
        this.remark=remark;
        this.email=email;
    }

}