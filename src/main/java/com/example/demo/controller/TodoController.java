package com.example.demo.controller;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.fasterxml.jackson.databind.util.JSONPObject;
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
import javax.servlet.http.HttpServletRequest;


import com.example.demo.domein.Todo;
import com.example.demo.repository.TodoRepository;
import com.example.demo.service.TodoService;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;


@Controller
@RequestMapping("")
public class TodoController {

    private String CLIENT_ID = "23665ab2523ccb26f74b";
    private String CLIENT_SECRET = "533f22821efc30b22ae014c8830640bbd68d8d38";

    @Autowired
    private TodoService todoService;

    @Autowired
    TodoRepository todoRepository;

    @GetMapping("/")
    public RedirectView home() {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("https://github.com/login/oauth/authorize?client_id=" + CLIENT_ID);
        return redirectView;
    }

    @GetMapping("/callback0")
    public String home0(HttpServletRequest request) throws MalformedURLException,
            ProtocolException, IOException {
        String code = request.getParameter("code");
        System.out.println(code);

        String url = "https://github.com/login/oauth/access_token";
        URL myurl = new URL(url);

        Map<String,Object> params = new LinkedHashMap<>();
        params.put("code", code);
        params.put("client_id", CLIENT_ID);
        params.put("client_secret", CLIENT_SECRET);

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String,Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);

        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

        for (int c; (c = in.read()) >= 0;)
            System.out.print((char)c);

        System.out.println(conn.getResponseCode());
        return "/todos/home";
    }

    @GetMapping("/todos")
    public String index(Model model) {
        List<Todo> todos = todoService.findAll();
        model.addAttribute("todos", todos); 
        return "todos/index"; 
    }

    @GetMapping("/todos/search")
    public String search_and_see_screen(Model model) {
      return "todos/search_and_see";
    }

    @PostMapping("/todos/search")
    public String search(Model model, @RequestParam String query) {
      List<Todo> todos = todoRepository.findTodosByName(query);
      model.addAttribute("todos", todos); 
      return "todos/search_and_see";
    }

    @GetMapping("/todos/new")
    public String newTodo(Model model) {
        return "todos/new";
    }

    @GetMapping("/todos/{id}/edit")
    public String edit(@PathVariable Long id, Model model) { 
        Todo todo = todoService.findOne(id);
        model.addAttribute("todo", todo);
        return "todos/edit";
    }

    @GetMapping("/todos/{id}")
    public String show(@PathVariable Long id, Model model) {
        Todo todo = todoService.findOne(id);
        model.addAttribute("todo", todo);
        return "todos/show";
    }

    @PostMapping("/todos/new")
    public String create(@ModelAttribute Todo todo) { 
        todoService.save(todo);
        return "redirect:/todos"; 
    }

    @PostMapping("/todos/{id}")
    public String done(@PathVariable Long id) {
        Todo todo = todoService.findOne(id);
        todo.setDone();
        todoService.save(todo);
        return "redirect:/todos";
    }

    @PutMapping("/todos/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Todo todo) {
        todo.setId(id);
        todoService.save(todo);
        return "redirect:/todos";
    }

    @DeleteMapping("/todos/{id}")
    public String destroy(@PathVariable Long id) {
        todoService.delete(id);
        return "redirect:/todos";
    }
    
}
