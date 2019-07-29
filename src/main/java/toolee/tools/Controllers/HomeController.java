package toolee.tools.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import toolee.tools.Models.AppUser;
import toolee.tools.Repositories.UserRepository;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


public class HomeController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

     public String[] cities = {"Seattle", "Spokane","Tacoma", "Vancouver","Bellevue", "Kent", "Everett", "Renton", "Federal Way", "Kirkland",
           "Auburn", "Shoreline"};

    @GetMapping("/register")
    public String showRegistrationPage(Model m, Principal p){
        m.addAttribute("principle", p);
        m.addAttribute("cities", cities);
        return "register";
    }

    @PostMapping("/register")
    public String addNewUser(Model m, Principal p, String username, String password, String city, String phoneNumber, String email){
        if(!checkUserName(username)){
            AppUser newUser = new AppUser(username, passwordEncoder.encode(password), city, phoneNumber, email);
            userRepository.save(newUser);
            Authentication authentication = new UsernamePasswordAuthenticationToken(newUser, null, new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            List<AppUser> users = (List) userRepository.findAll();
            return "redirect:/discover";
        }else{
            m.addAttribute("error", "notUnique");
            m.addAttribute("principle", p);
            return "register";
        }
    }


    //**************************************** Helper Functions **************************************

    public boolean checkUserName(String username){
        Iterable<AppUser> allUsers =  userRepository.findAll();
        List<String> allUsername = new ArrayList<>();

        for(AppUser appUser : allUsers){
            allUsername.add(appUser.getUsername());
        }

        if(allUsername.contains(username)){
            return true;
        }else{
            return false;
        }
    }

}
