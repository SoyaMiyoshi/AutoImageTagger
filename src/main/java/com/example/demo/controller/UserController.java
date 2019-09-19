package com.example.demo.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import com.example.demo.domein.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

import javax.imageio.ImageIO;

@Controller
@RequestMapping("")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/")
    public String home(Model model) {
        return "users/upload";
    }

//    @GetMapping("/todos")
//    public String index(Model model) {
//        List<User> todos = userService.findAll();
//        model.addAttribute("todos", todos);
//        return "/todos/upload";
//    }
//
//    @GetMapping("/todos/search")
//    public String search_and_see_screen(Model model) {
//      return "todos/search_and_see";
//    }
//
//    @PostMapping("/todos/search")
//    public String search(Model model, @RequestParam String query) {
//      List<User> todos = userRepository.findUsersByName(query);
//      model.addAttribute("todos", todos);
//      return "todos/search_and_see";
//    }
//
//    @GetMapping("/todos/new")
//    public String newTodo(Model model) {
//        return "todos/new";
//    }
//
//    @GetMapping("/todos/{id}/edit")
//    public String edit(@PathVariable Long id, Model model) {
//        User todo = userService.findOne(id);
//        model.addAttribute("todo", todo);
//        return "todos/edit";
//    }
//
//    @GetMapping("/todos/{id}")
//    public String show(@PathVariable Long id, Model model) {
//        User todo = userService.findOne(id);
//        model.addAttribute("todo", todo);
//        return "todos/show";
//    }
//
//    @PostMapping("/todos/new")
//    public String create(@ModelAttribute User todo) {
//        userService.save(todo);
//        return "redirect:/todos";
//    }
//
//
    @PostMapping("/upload")
    public String handleFormUpload( @RequestParam("file") MultipartFile file, @RequestParam("username") String username) throws IOException {
        User user = new User();
        user.setName(username);
        userService.save(user);

        if (!file.isEmpty()) {
            BufferedImage src = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
            File destination = new File("/Users/miyoshisoya/j/mytodoapp/src/main/resources/stored/" + user.getId().toString() + ".jpg"); // something like C:/Users/tom/Documents/nameBasedOnSomeId.png
            ImageIO.write(src, "jpg", destination);
        }

        return "users/upload";
    }

    
}
