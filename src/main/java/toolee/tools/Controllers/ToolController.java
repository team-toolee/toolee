package toolee.tools.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

import java.util.ArrayList;
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
        AppUser user = userRepository.findByUsername(p.getName());
        m.addAttribute("principal",user);
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

    @GetMapping("/tool/{id}/edit")
    public String editAccount(@PathVariable long id, Model m, Principal p){
        //get the information for the selected account
        List<String> accountType = new ArrayList<>();
        Tool tool = toolRepository.findById(id).get();
        AppUser user = userRepository.findByUsername(p.getName());
        m.addAttribute("principal", user);
        m.addAttribute("editTool", tool);
        //TO DO: need to create categories and cities
        return "editAccount";
    }

    @PostMapping(value = "/account/{id}/edit")
    public String editAccount(@RequestParam Long id, String name, String price, String status, String description, String category, Principal p, Model m) {
        try {
            Tool updatedTool = toolRepository.findById(id).get();
            String message = "Successfully edited the tool: "+ name;

            updatedTool.setName(name);
            updatedTool.setDescription(description);
            updatedTool.setPrice(Double.parseDouble(price));
            updatedTool.setStatus(Status.valueOf(status));
            updatedTool.setCategory(Category.valueOf(category));
            toolRepository.save(updatedTool);
            AppUser user = userRepository.findByUsername(p.getName());
            m.addAttribute("principal",user);
            m.addAttribute("message",message);

            return "profile";
        } catch (Exception error) {
            return "An error has occurred: " + error;
        }
    }

    @GetMapping("/tool/{id}/delete")
    public String deleteTool(@PathVariable long id, Model m, Principal p){
        //get the information for the selected account
        List<String> accountType = new ArrayList<>();
        Tool tool = toolRepository.findById(id).get();
        AppUser user = userRepository.findByUsername(p.getName());
        m.addAttribute("principal", user);
        m.addAttribute("delTool", tool);
        return "deleteAccount";
    }
    @PostMapping("/tool/{id}/delete")
    public String deleteTool(@RequestParam long id, Model m, Principal p,Integer temp){
        try{
            // to display information of selected account to be deleted
            Tool tool = toolRepository.findById(id).get();
            String message = "Successfully deleted the tool: "+ tool.getName();
            AppUser user = userRepository.findByUsername(p.getName());

            toolRepository.delete(tool);

            m.addAttribute("principal",user);
            m.addAttribute("message",message);


            return "profile";
        } catch (Exception error){
            return "An error has occurred: " + error;
        }
    }


}
