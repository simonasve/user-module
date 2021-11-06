package vu.lt.usermodule.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import vu.lt.usermodule.model.User;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findAll_findAllUsers_hasSizeOf0() {
        assertThat(userRepository.findAll()).hasSize(0);
    }

    @Test
    void findById_findSingleUserById_doesNotFindUser() {
        User user = userRepository.findById(1L).orElse(null);

        assertThat(user).isEqualTo(null);
    }

    @Test
    void save_saveNewUser_savesNewUser() {
        User newUser = new User();
        newUser.setName("Tomas");
        newUser.setSurname("Tomaitis");
        newUser.setPhoneNumber("8612345678");
        newUser.setAddress("Namu g. 3");
        newUser.setEmailAddress("tomas@gmail.com");
        newUser.setPassword("Password123$");

        User user = userRepository.save(newUser);

        assertThat(user).hasFieldOrPropertyWithValue("name", "Tomas");
    }

    @Test
    void deleteById_deleteUser_deletesUser() {
        userRepository.deleteById(1L);

        assertThat(userRepository.findAll()).hasSize(0);
    }
}
