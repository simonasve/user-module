package vu.lt.usermodule.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import vu.lt.usermodule.model.User;
import vu.lt.usermodule.repository.UserRepository;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private static final List<User> users = new ArrayList<>();

    @BeforeAll
    private static void beforeAll() {
        for (int i = 0; i < 5; i++) {
            users.add(new User());
        }
    }

    @Test
    void getAllUsers_findAllUsers_Equals() {
        when(userRepository.findAll()).thenReturn(users);

        List<User> foundUsers = userService.getAllUsers();

        verify(userRepository).findAll();

        assertEquals(5, foundUsers.size());
    }

    @Test
    void getUserById_findSingleUserById_NotNull() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.ofNullable(users.get(Mockito.anyInt())));

        User foundUser = userService.getUserById(2L);

        verify(userRepository).findById(Mockito.anyLong());

        assertNotNull(foundUser);
    }

    @Test
    void saveUser_saveNewUser_NotNull() {
        User user = new User();

        user.setName("Tomas");
        user.setSurname("Tomaitis");
        user.setPhoneNumber("8612345678");
        user.setAddress("Namu g. 3");
        user.setEmailAddress("tomas@gmail.com");
        user.setPassword("Password123$");

        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        User savedUser = userService.saveUser(user);

        verify(userRepository).save(Mockito.any(User.class));

        assertNotNull(savedUser);
    }

    @Test
    void saveUser_saveNewUserWithBadPassword_Null() {
        User user = new User();

        user.setName("Tomas");
        user.setSurname("Tomaitis");
        user.setPhoneNumber("8612345678");
        user.setAddress("Namu g. 3");
        user.setEmailAddress("tomas@gmail.com");
        user.setPassword("password123$");

        User savedUser = userService.saveUser(user);

        assertNull(savedUser);
    }

    @Test
    void saveUser_saveNewUserWithBadEmail_Null() {
        User user = new User();

        user.setName("Tomas");
        user.setSurname("Tomaitis");
        user.setPhoneNumber("8612345678");
        user.setAddress("Namu g. 3");
        user.setEmailAddress("tomasgmail.com");
        user.setPassword("Password123$");

        User savedUser = userService.saveUser(user);

        assertNull(savedUser);
    }

    @Test
    void saveUser_saveNewUserWithBadPhoneNumber_Null() {
        User user = new User();

        user.setName("Tomas");
        user.setSurname("Tomaitis");
        user.setPhoneNumber("861234567");
        user.setAddress("Namu g. 3");
        user.setEmailAddress("tomas@gmail.com");
        user.setPassword("Password123$");

        User savedUser = userService.saveUser(user);

        assertNull(savedUser);
    }

    @Test
    void deleteUser_deleteUserById_CalledOneTime() {
        userService.deleteUser(3L);
        verify(userRepository).deleteById(Mockito.anyLong());
    }
}
