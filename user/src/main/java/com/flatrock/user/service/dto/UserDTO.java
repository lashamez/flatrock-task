package com.flatrock.user.service.dto;

import com.flatrock.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A DTO representing a user, with only the public attributes.
 */
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {

    private Long id;

    private String login;

    public UserDTO(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
    }

}
