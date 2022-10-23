package pl.pwr.peaklogistic.controller;


import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.pwr.peaklogistic.dto.request.UserPasswordRequest;
import pl.pwr.peaklogistic.dto.request.UserRequest;
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

    @GetMapping(value = "/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable long id){
        return userRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/users")
    public ResponseEntity<?> createUser(@RequestBody UserRequest userRequest){
        User addedUser = userRepository.save(UserRequest.toDomain(userRequest));
        return ResponseEntity.created(URI.create("/" + addedUser.getUserID())).body(addedUser);
    }


    @Transactional
    @PutMapping(value = "/users/{id}")
    public ResponseEntity<?> updatePassword(@PathVariable long id, @RequestBody UserPasswordRequest userPasswordRequest){
        if(!userRepository.existsById(id))
            return ResponseEntity.notFound().build();
        else{
            User user = userRepository.getReferenceById(id);
            user.setPassword(userPasswordRequest.getPassword());
            return ResponseEntity.ok().body(userRepository.save(user));
        }
    }

    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id){
        if(!userRepository.existsById(id))
            return ResponseEntity.notFound().build();
        else{
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
    }
}