package Pbo.GameStore.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import Pbo.GameStore.Dto.UserDto;
import Pbo.GameStore.Models.User;
import Pbo.GameStore.Repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private UserRepository userRepository;


    @Override
    public User save(UserDto userDto) {
        String role = "USER";
        User user = new User(userDto.getEmail(), passwordEncoder.encode(userDto.getPassword()), role, userDto.getFullname());
        return userRepository.save(user);
    }
}
