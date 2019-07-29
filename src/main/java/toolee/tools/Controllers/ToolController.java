package toolee.tools.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import toolee.tools.Enums.Category;
import toolee.tools.Enums.Status;

import toolee.tools.Models.AppUser;
import toolee.tools.Models.Tool;
import toolee.tools.Repositories.ToolRepository;
import toolee.tools.Repositories.UserRepository;


import java.security.Principal;
import java.util.List;

public class ToolController {
    @Autowired
    ToolRepository toolRepository;
    @Autowired
    UserRepository userRepository;
    @GetMapping("/home")
    public String getHome(Principal p, Model m) {
        List<Tool> tool = (List) toolRepository.findAll();
       m.addAttribute("principal",p);
        return "home";
    }


    @PostMapping("/tool/add")
    public String addtool(@RequestParam String name, @RequestParam String imageUrl, @RequestParam double price,

                           @RequestParam Status status, @RequestParam String description, Category category, Principal p) {
        AppUser user = userRepository.findByUsername(p.getName());
        Tool newTool = new Tool(name, imageUrl, price, status, description, category);

        toolRepository.save(newTool);

        List<Tool> tool = (List) toolRepository.findAll();
        return "home";
    }


}
