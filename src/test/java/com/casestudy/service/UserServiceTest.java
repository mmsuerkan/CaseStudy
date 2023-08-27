package com.casestudy.service;

import com.casestudy.dto.user.UserRequestDto;
import com.casestudy.dto.user.UserResponseDto;
import com.casestudy.exception.FirstAndLastNameNotEmptyException;
import com.casestudy.exception.UserAlreadyExistException;
import com.casestudy.model.User;
import com.casestudy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testCreateUser_Success() throws FirstAndLastNameNotEmptyException, UserAlreadyExistException {
        UserRequestDto requestDto = new UserRequestDto();
        requestDto.setFirstName("John");
        requestDto.setLastName("Doe");

        User newUser = new User();
        newUser.setId(1);
        newUser.setFirstName(requestDto.getFirstName());
        newUser.setLastName(requestDto.getLastName());
        newUser.setCreatedAt(LocalDate.now());

        when(userRepository.findByFirstNameAndLastName(anyString(), anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        UserResponseDto responseDto = userService.createUser(requestDto);

        assertNotNull(responseDto);
        assertEquals(newUser.getId(), responseDto.getId().intValue());
        assertEquals(newUser.getFirstName(), responseDto.getFirstName());
        assertEquals(newUser.getLastName(), responseDto.getLastName());
        assertEquals(newUser.getCreatedAt(), responseDto.getCreatedAt());
    }

    @Test
    public void testCreateUser_UserAlreadyExists() {
        UserRequestDto requestDto = new UserRequestDto();
        requestDto.setFirstName("John");
        requestDto.setLastName("Doe");

        when(userRepository.findByFirstNameAndLastName(anyString(), anyString()))
                .thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistException.class, () -> userService.createUser(requestDto));
    }
}