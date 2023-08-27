package com.casestudy.service;

import com.casestudy.dto.user.UserRequestDto;
import com.casestudy.dto.user.UserResponseDto;
import com.casestudy.exception.FirstAndLastNameNotEmptyException;
import com.casestudy.exception.UserAlreadyExistException;
import com.casestudy.model.User;
import com.casestudy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDto createUser(UserRequestDto userRequestDto) throws FirstAndLastNameNotEmptyException, UserAlreadyExistException {

        if(userRequestDto.getFirstName().isEmpty() || userRequestDto.getLastName().isEmpty()) {
            throw new FirstAndLastNameNotEmptyException("First name and last name cannot be empty");
        }

        //user already exists
        if(userRepository.findByFirstNameAndLastName(userRequestDto.getFirstName(), userRequestDto.getLastName()).isPresent()) {
            throw new UserAlreadyExistException("User already exists");
        }

        User newUser = new User();
        newUser.setFirstName(userRequestDto.getFirstName());
        newUser.setLastName(userRequestDto.getLastName());


        newUser.setCreatedAt(LocalDate.now());

        User savedUser = userRepository.save(newUser);

        return mapUserToUserResponseDto(savedUser);
    }

    private UserResponseDto mapUserToUserResponseDto(User user) {

        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(Long.valueOf(user.getId()));
        responseDto.setFirstName(user.getFirstName());
        responseDto.setLastName(user.getLastName());
        responseDto.setCreatedAt(user.getCreatedAt());

        return responseDto;
    }
}
