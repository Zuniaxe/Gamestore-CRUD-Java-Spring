package Pbo.GameStore.services;

import Pbo.GameStore.Dto.UserDto;
import Pbo.GameStore.Models.User;

public interface UserService {
    User save(UserDto userDto);
}
