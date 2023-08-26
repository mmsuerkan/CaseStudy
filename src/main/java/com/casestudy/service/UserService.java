package com.casestudy.service;

import com.casestudy.repository.UserRepository;
import com.casestudy.dto.user.UserRequestDto;
import com.casestudy.dto.user.UserResponseDto;
import com.casestudy.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        User newUser = new User();
        newUser.setFirstName(userRequestDto.getFirstName());
        newUser.setLastName(userRequestDto.getLastName());

        LocalDateTime now = LocalDateTime.now();
        newUser.setCreatedAt(Timestamp.valueOf(now));

        User savedUser = userRepository.save(newUser);

        return mapUserToUserResponseDto(savedUser);
    }

    private UserResponseDto mapUserToUserResponseDto(User user) {

        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(Long.valueOf(user.getId()));
        responseDto.setFirstName(user.getFirstName());
        responseDto.setLastName(user.getLastName());
        responseDto.setCreatedAt(user.getCreatedAt().toLocalDateTime());

        return responseDto;
    }
}
