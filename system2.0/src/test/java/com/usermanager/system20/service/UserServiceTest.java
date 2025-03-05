package com.usermanager.system20.service;


import com.usermanager.system20.entity.UserEntity;
import com.usermanager.system20.exceptions.UserAlreadyExistsException;
import com.usermanager.system20.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("Gustavo Queiros");
        userEntity.setAge((short) 22);
        userEntity.setEmail("gustavo@email.com");
        userEntity.setPassword("password123");
    }

    @Test
    @DisplayName("Must successfully create a user")
    void testCreateUser_Sucess() {

        when(userRepository.existsByEmail(userEntity.getEmail())).thenReturn(false);

        when(userRepository.save(userEntity)).thenReturn(userEntity);

        UserEntity createdUser = userService.createUser(userEntity);

        assertNotNull(createdUser);
        assertEquals(userEntity.getId(), createdUser.getId());
        assertEquals(userEntity.getEmail(), createdUser.getEmail());
        assertEquals(userEntity.getName(), createdUser.getName());

        verify(userRepository, times(1)).existsByEmail(userEntity.getEmail());
        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    @DisplayName("Must throw exception when email already exixts")
    void testeCreateUser_UserAlreadyExists() {
        when(userRepository.existsByEmail(userEntity.getEmail())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(userEntity));

        verify(userRepository, times(1)).existsByEmail(userEntity.getEmail());
        verify(userRepository, never()).save(any(UserEntity.class));
    }


}
