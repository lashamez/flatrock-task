package com.flatrock.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long userId;
    private String phone;
    private String email;
}
