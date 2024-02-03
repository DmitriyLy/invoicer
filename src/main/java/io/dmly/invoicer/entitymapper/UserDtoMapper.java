package io.dmly.invoicer.entitymapper;

import io.dmly.invoicer.dto.UserDto;
import io.dmly.invoicer.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {
    @Mapping(target = "isEnabled", expression = "java(user.isEnabled())")
    @Mapping(target = "isNonLocked", expression = "java(user.isNonLocked())")
    @Mapping(target = "isUsingMfa", expression = "java(user.isUsingMfa())")
    @Mapping(target = "roleName", source = "role.name")
    @Mapping(target = "permissions", source = "role.permission")
    UserDto fromUser(User user);
}
