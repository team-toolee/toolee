package toolee.tools.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import toolee.tools.Config.S3Client;
import toolee.tools.Enums.Category;
import toolee.tools.Enums.Status;

import toolee.tools.Models.AppUser;
import toolee.tools.Models.Tool;
import toolee.tools.Repositories.ToolRepository;
import toolee.tools.Repositories.UserRepository;

import java.io.IOException;
import java.security.Principal;

import java.util.List;

@Controller
public class ToolController {
    @Autowired
    ToolRepository toolRepository;
    @Autowired
    UserRepository userRepository;

    private S3Client s3Client;

    @Autowired
    ToolController(S3Client s3Client){this.s3Client = s3Client;}

    @GetMapping("/home")
    public String getHome(Principal p, Model m) {
        List<Tool> tool = (List) toolRepository.findAll();
       m.addAttribute("principal",p);
        return "home";
    }


    @PostMapping("/tool/add")
    public String addtool(@RequestParam String name, @RequestParam(value = "file")MultipartFile file, @RequestParam String price,

                          @RequestParam Status status, @RequestParam String description, Category category, Principal p) throws IOException {
        String imageUrl = this.s3Client.uploadFile(file);
        AppUser user = userRepository.findByUsername(p.getName());
        Tool newTool = new Tool(name, imageUrl, Double.parseDouble(price), status, description, category);
        user.getTools().add(newTool);
        userRepository.save(user);
        toolRepository.save(newTool);

        return "home";
    }



}
