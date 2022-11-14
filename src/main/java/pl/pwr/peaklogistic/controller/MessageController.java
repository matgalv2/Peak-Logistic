package pl.pwr.peaklogistic.controller;


import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pwr.peaklogistic.repository.MessageRepository;

@AllArgsConstructor
@RestController
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    private final MessageRepository messageRepository;

//    // only for testing
//    @GetMapping(value = "/messages")
//    public ResponseEntity<?> getAllMessages(){
//        return ResponseEntity.ok(messageRepository.findAll());
//    }
//
//    @GetMapping(value = "/messages/{id}")
//    public ResponseEntity<?> getUserById(@PathVariable long id){
//        return messageRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
//    }

//    @PostMapping(value = "/messages")
//    public ResponseEntity<?> createUser(@RequestBody MessageRequest messageRequest){
//        User addedUser = messageRepository.save(MessageRequest.toDomain(messageRequest));
//        return ResponseEntity.created(URI.create("/" + addedUser.getUserID())).body(addedUser);
//    }
//
//
//    @Transactional
//    @PutMapping(value = "/users/{id}")
//    public ResponseEntity<?> updatePassword(@RequestParam long id, @RequestBody UserPasswordRequest userPasswordRequest){
//        if(!messageRepository.existsById(id))
//            return ResponseEntity.notFound().build();
//        else{
//            User user = messageRepository.getReferenceById(id);
//            user.setPassword(userPasswordRequest.getPassword());
//            return ResponseEntity.ok().body(messageRepository.save(user));
//        }
//    }
//
//    @DeleteMapping(value = "/users/{id}")
//    public ResponseEntity<?> deleteUser(@RequestParam long id){
//        if(!messageRepository.existsById(id))
//            return ResponseEntity.notFound().build();
//        else{
//            messageRepository.deleteById(id);
//            return ResponseEntity.noContent().build();
//        }
//    }
}
