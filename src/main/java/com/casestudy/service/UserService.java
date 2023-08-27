package com.casestudy.service;

import com.casestudy.exception.FirstAndLastNameNotEmptyException;
import com.casestudy.exception.UserAlreadyExistException;
import com.casestudy.repository.UserRepository;
import com.casestudy.dto.user.UserRequestDto;
import com.casestudy.dto.user.UserResponseDto;
import com.casestudy.model.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    public UserResponseDto createUser(UserRequestDto userRequestDto) throws FirstAndLastNameNotEmptyException, UserAlreadyExistException {

        if(userRequestDto.getFirstName().isEmpty() || userRequestDto.getLastName().isEmpty()) {
            logger.atError().log("First name and last name cannot be empty");
            throw new FirstAndLastNameNotEmptyException("First name and last name cannot be empty");
        }

        //user already exists
        if(userRepository.findByFirstNameAndLastName(userRequestDto.getFirstName(), userRequestDto.getLastName()).isPresent()) {
            logger.atError().log("User already exists");
            throw new UserAlreadyExistException("User already exists");
        }

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
