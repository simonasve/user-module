package vu.lt.usermodule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vu.lt.usermodule.model.User;
import vu.lt.usermodule.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    private List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    private ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        User user = userService.getUserById(id);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping()
    private ResponseEntity<User> saveUser(@RequestBody User user) {
        User savedUser = userService.saveUser(user);

        if (savedUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(savedUser, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    private void deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
    }
}
