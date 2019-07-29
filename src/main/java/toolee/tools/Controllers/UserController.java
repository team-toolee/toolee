package toolee.tools.Controllers;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.view.RedirectView;
import toolee.tools.Enums.Status;
import toolee.tools.Models.AppUser;
import toolee.tools.Models.Tool;
import toolee.tools.Repositories.ToolRepository;
import toolee.tools.Repositories.UserRepository;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ToolRepository toolRepository;

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

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @PostMapping(value = "/login")
    public RedirectView loginUsers(@RequestParam String username, String password) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new RedirectView("/user");
    }



    @GetMapping("/profile")
    public String getProfile(Model m, Principal principal){
        AppUser user = userRepository.findByUsername(principal.getName());
        m.addAttribute("profile", user);
        m.addAttribute("principal", principal);
        return "profile";
    }


    @PutMapping("tool/status/{id}")
    public String updateStatus(@PathVariable Long id, Model m, Principal principal){
        Optional<Tool> tool = toolRepository.findById(id);

        if(tool.get().getStatus() == Status.Available){
            tool.get().setStatus(Status.Rented);
        }
        else{
            tool.get().setStatus(Status.Available);
        }
        toolRepository.save(tool.get());
        return "profile";
    }




}
