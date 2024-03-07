package com.authentication.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceAlreadyExistsException extends RuntimeException{

    String str1;
    String fieldName;

    String str2;

    public ResourceAlreadyExistsException(String str1,String fieldName,String str2)
    {
        super(String.format("%s %s %s",str1,fieldName,str2));
        this.str1=str1;
        this.fieldName=fieldName;
        this.str2=str2;
    }
}
