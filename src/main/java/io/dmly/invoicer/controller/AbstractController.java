package io.dmly.invoicer.controller;

import io.dmly.invoicer.dto.UserDto;
import io.dmly.invoicer.entitymapper.UserDtoMapper;
import io.dmly.invoicer.model.InvoicerUserDetails;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractController {

    protected UserDtoMapper userDtoMapper;

    protected UserDto getUserDto(InvoicerUserDetails userDetails) {
        return userDtoMapper.fromUser(userDetails.getUser());
    }

    @Autowired
    public void setUserDtoMapper(UserDtoMapper userDtoMapper) {
        this.userDtoMapper = userDtoMapper;
    }
}
