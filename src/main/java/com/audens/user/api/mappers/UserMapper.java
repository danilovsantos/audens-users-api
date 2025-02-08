package com.audens.user.api.mappers;

import com.audens.user.api.dto.output.user.RecoveryUserDto;
import com.audens.user.api.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Named("mapUserToUserDto")
    RecoveryUserDto mapUserToUserDto(User user);

}
