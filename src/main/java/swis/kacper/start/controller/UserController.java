package swis.kacper.start.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swis.kacper.start.model.Role;
import swis.kacper.start.model.User;
import swis.kacper.start.service.UserService;

import java.util.Locale;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody User user){

        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.OK);
    }


    @GetMapping("/hello")
    public String hello(){
        return "Im Spring Boot controller";
    }



    @GetMapping("/users")
    public ResponseEntity<?> getUsers(){

        return new ResponseEntity<>(userService.findAllUsers(), HttpStatus.OK);
    }


    @DeleteMapping  ("/{number}")
    public ResponseEntity <?>  deleteUser(@PathVariable Integer number){
        userService.deleteUser((number));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping ("/change/{email}/{role}")
    public ResponseEntity <?> changeRole(@PathVariable String email, @PathVariable String role){
        if(role.toUpperCase(Locale.ROOT).equals("ADMIN"))
        userService.changeRole(email,Role.ADMIN);
        else
        userService.changeRole(email,Role.USER);
        return new ResponseEntity<>(HttpStatus.OK);
    }




}
