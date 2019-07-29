package toolee.tools.Controllers;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.sun.org.apache.xpath.internal.operations.Mod;
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
import java.util.*;


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

    @PostMapping("/contact")
    public String contactToolSeller(Model m, Principal p, long toolSellerId, long ToolId, String message){
        Optional<AppUser> sellerOptional = userRepository.findById(toolSellerId);
        AppUser seller = sellerOptional.get();



        return "redirect:/discover";
    }

    //********************************************************************* Helper Functions ******************************************************************

    //SNS function to help me send messages to a person once a task has been assigned
    public static void configureMessage(String phoneNumber, String message) {
        AmazonSNSClient snsClient = new AmazonSNSClient();
        message = "A task has been assigned to you";
        Map<String, MessageAttributeValue> smsAttributes =
                new HashMap<>();
        //<set SMS attributes>
        sendSMSMessage(snsClient, message, phoneNumber, smsAttributes);
    }

    public static void sendSMSMessage(AmazonSNSClient snsClient, String message,
                                      String phoneNumber, Map<String, MessageAttributeValue> smsAttributes) {
        PublishResult result = snsClient.publish(new PublishRequest()
                .withMessage(message)
                .withPhoneNumber(phoneNumber)
                .withMessageAttributes(smsAttributes));
        System.out.println(result); // Prints the message ID.
    }


}
