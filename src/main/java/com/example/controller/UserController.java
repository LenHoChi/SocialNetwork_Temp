package com.example.controller;

import com.example.dto.UserDTO;
import com.example.exception.BadRequestException;
import com.example.service.RelationshipService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RelationshipService relationshipService;
    @GetMapping("")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> findAllUsers(){
        List<UserDTO> listUserDTO = userService.findAllUsers();
        if(listUserDTO.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @PostMapping("")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO) throws Exception {
        return ResponseEntity.ok(userService.saveUser(userDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable(value = "id") String id) throws Exception {
        if(relationshipService.checkMail(id)) {
            return new ResponseEntity<>(userService.deleteUser(id), HttpStatus.OK);
        }else{
            throw new BadRequestException("Email not accepted");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findUserById(@PathVariable(value = "id") String id) throws Exception {
        if(relationshipService.checkMail(id)) {
            return ResponseEntity.ok(userService.findUserById(id));
        }else{
            throw new BadRequestException("Email not accept");
        }
    }
}
