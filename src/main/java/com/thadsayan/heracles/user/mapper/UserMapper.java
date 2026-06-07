package com.thadsayan.heracles.user.mapper;

import com.thadsayan.heracles.user.User;
import com.thadsayan.heracles.user.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User user);
}
