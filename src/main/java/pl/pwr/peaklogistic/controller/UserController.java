package pl.pwr.peaklogistic.controller;


import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pwr.peaklogistic.dto.UserRequest;
import pl.pwr.peaklogistic.model.User;
import pl.pwr.peaklogistic.repository.UserRepository;

import java.net.URI;

@AllArgsConstructor
@RestController
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserRepository userRepository;

    @GetMapping(value = "/users")
    public ResponseEntity<?> getAllUsers(){
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping(value = "/users?id={id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id){
        return userRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/users")
    public ResponseEntity<?> createUser(@RequestBody User user){
        User addedUser = userRepository.save(user);
        return ResponseEntity.created(URI.create("/" + addedUser.getUserID())).body(addedUser);
    }


//    @PutMapping(value = "/users")
//    public ResponseEntity<?> updatePassword(@RequestBody UserRequest user){
//
//    }
}