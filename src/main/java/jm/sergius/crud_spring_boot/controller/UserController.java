package jm.sergius.crud_spring_boot.controller;

import jm.sergius.crud_spring_boot.domain.Role;
import jm.sergius.crud_spring_boot.domain.User;
import jm.sergius.crud_spring_boot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;


import java.util.Set;

@Controller
public class UserController {
    private UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping(value = "/admin")
    public String getAllUsers(ModelMap model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("listOfUsers", userRepository.findAll());
        model.addAttribute("role_u", new Role("ROLE_USER"));
        model.addAttribute("role_a", new Role("ROLE_ADMIN"));
        return "index";
    }

    @PostMapping(value = "/admin/edit")
    public String updateUser(@ModelAttribute("user") User user, @ModelAttribute("role1") Role role1,
                             @ModelAttribute("role2") Role role2) {

        Set<Role> rSet;
        if(role2.getRole() != null){
            rSet = Set.of(role1, role2);
        } else {
            rSet = Set.of(role1);
        }

        user.setRoles(rSet);
        userRepository.saveAndFlush(user);
        return "redirect:/admin";
    }

    @PostMapping(value = "/admin/add")
    public String saveUser(@ModelAttribute("user") User user, @ModelAttribute("role1") Role role1,
                           @ModelAttribute("role2") Role role2){
        Set<Role> rSet;
        if(role2.getRole() != null){
            rSet = Set.of(role1, role2);
        } else {
            rSet = Set.of(role1);
        }

        user.setRoles(rSet);
        userRepository.saveAndFlush(user);
        return "redirect:/admin";
    }

    @GetMapping(value="/admin/delete")
    public String deleteUser(@RequestParam long id) {
        userRepository.deleteById(id);
        return "redirect:/admin";
    }

    @GetMapping(value = "/admin/clearAll")
    public String clearAll() {
        userRepository.deleteAll();
        return "redirect:/admin";
    }

    @GetMapping(value = "/user")
    public String getUserinfo(ModelMap model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        model.addAttribute("role_u", new Role("ROLE_USER"));
        model.addAttribute("role_a", new Role("ROLE_ADMIN"));
        return "userinfo";
    }

    @GetMapping(value = "/")
    public String initPage() {
        return "redirect:/login";
    }
}
