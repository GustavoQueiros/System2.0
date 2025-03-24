package com.usermanager.system20.service;


import com.usermanager.system20.entity.UserEntity;
import com.usermanager.system20.exceptions.UserAlreadyExistsException;
import com.usermanager.system20.exceptions.UserListEmptyException;
import com.usermanager.system20.exceptions.UserNotFoundException;
import com.usermanager.system20.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;


import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
        userEntity = new UserEntity(1L, "Gustavo Queiros", (short) 22, "gustavo@email.com", "password123");
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

    @Test
    @DisplayName("Must successfully list users")
    void testListUser_Success() throws UserListEmptyException {
        when(userRepository.findAll()).thenReturn(List.of(userEntity));

        List<UserEntity> users = userService.listUser();
        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        verify(userRepository, times(1)).findAll();
    }


    @Test
    @DisplayName("Must throw exception when no users are found")
    void testListUser_EmptyList() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(UserListEmptyException.class, () -> userService.listUser());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Must successfully get user by ID")
    void testGetUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        Optional<UserEntity> user = userService.getUserById(1L);
        assertTrue(user.isPresent());
        assertEquals(userEntity.getId(), user.get().getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Must return empty optional when user is not found")
    void testGetUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<UserEntity> user = userService.getUserById(1L);
        assertTrue(user.isEmpty());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Must successfully update user")
    void testUpdateUser_Success() throws UserNotFoundException{
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        Optional<UserEntity> updatedUser = userService.updateUser(userEntity, 1L);

        assertTrue(updatedUser.isPresent());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Must throw exception when trying to update non-existing user")
    void testUpdateUser_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(userEntity, 1L));
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Must successfully delete user")
    void testDeleteUser_Success() throws UserNotFoundException{
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        doNothing().when(userRepository).deleteById(1L);

        Optional<UserEntity> deletedUser = userService.deleteUser(1L);
        assertTrue(deletedUser.isPresent());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Must throw exception when trying to delete non-existing user")
    void testDeleteUser_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(1L));
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).deleteById(anyLong());
    }

}
