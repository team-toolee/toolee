package toolee.tools.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import toolee.tools.Models.AppUser;
import toolee.tools.Repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;



@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

//    @GetMapping("/tool")
//    public List<Tool> getTool(){
//        List<Tool> tools = (List) toolRepository.findAll();
//        return tools;
//    }

    @GetMapping("/user")
    public List<AppUser> getUser(){
        List<AppUser> users = (List) userRepository.findAll();
        return users;
    }

    @PostMapping("/user")
    public List<AppUser> addUser(@RequestParam String username, @RequestParam String password, @RequestParam String city, @RequestParam String phoneNumber, @RequestParam String email){
        AppUser newUser = new AppUser(username, passwordEncoder.encode(password), city, phoneNumber, email);
        userRepository.save(newUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(newUser, null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        List<AppUser> users = (List) userRepository.findAll();
        return users;
    }






}
