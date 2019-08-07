package udemy.learning.ppmtool.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import udemy.learning.ppmtool.entity.User;
import udemy.learning.ppmtool.service.UserService;
import udemy.learning.ppmtool.service.ValidationService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private ValidationService validationService;
    private UserService userService;

    public UserController(ValidationService validationService, UserService userService) {
        this.validationService = validationService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult result) {
        ResponseEntity<?> errorMap = validationService.validate(result);
        if (errorMap != null) return errorMap;

        User newUser = userService.saveUser(user);

        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }
}