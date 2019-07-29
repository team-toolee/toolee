package toolee.tools.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.crypto.password.PasswordEncoder;
import toolee.tools.Models.AppUser;
import toolee.tools.Models.Tool;
import toolee.tools.Repositories.ToolRepository;
import toolee.tools.Repositories.UserRepository;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;



@Controller
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ToolRepository toolRepository;

    @GetMapping("/discover")
    public String getToolsForPrincipleCity(Model m, Principal p){
        AppUser loggedInUser = userRepository.findByUsername(p.getName());
        String userCity = loggedInUser.getCity();
        List<AppUser> usersInCity = userRepository.findByCity(userCity);
        List<Tool> toolsInCity = new ArrayList<>();

        for(AppUser user: usersInCity){
            for(Tool tool: user.getTools()){
                toolsInCity.add(tool);
            }
        }

        m.addAttribute("toolsInCity", toolsInCity);
        m.addAttribute("principle", p);

        return "discover";
    }

    @PostMapping("/discover")
    public String getToolsForFilteredCity(Model m, Principal p, String city){
        List<AppUser> usersInCity = userRepository.findByCity(city);
        List<Tool> toolsInCity = new ArrayList<>();

        for(AppUser user: usersInCity){
            for(Tool tool: user.getTools()){
                toolsInCity.add(tool);
            }
        }

        m.addAttribute("toolsInCity", toolsInCity);
        m.addAttribute("principle", p);

        return "discover";
    }

}
