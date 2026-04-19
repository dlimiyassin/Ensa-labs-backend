package com.ensa.labs.zBase.bean;


import com.ensa.labs.zBase.security.bean.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseEntity {

    private User createdBy;
}
