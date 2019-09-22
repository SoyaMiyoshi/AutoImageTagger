package com.example.demo.controller;

import java.io.*;
import java.net.*;

import java.util.LinkedHashMap;
import java.util.List;

import com.example.demo.domein.Uploaded;
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

import com.example.demo.repository.UploadedRepository;
import com.example.demo.service.UploadedService;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;
import java.util.stream.Collectors;


@Controller
@RequestMapping("")
public class UploadedController {

    private String CLIENT_ID = "23665ab2523ccb26f74b";
    private String CLIENT_SECRET = "533f22821efc30b22ae014c8830640bbd68d8d38";
    private String login;

    public String myApiCall ( Map<String,Object> params,  String url, String method) throws IOException {

        URL myurl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
        conn.setRequestMethod(method);

        if (method.equals("POST")) {
            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String,Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }

            byte[] postDataBytes = postData.toString().getBytes("UTF-8");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        return(in.lines().collect(Collectors.joining()));
    }

    @Autowired
    private UploadedService uploadedService;

    @Autowired
    UploadedRepository uploadedRepository;

    @GetMapping("/")
    public RedirectView home() {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("https://github.com/login/oauth/authorize?client_id=" + CLIENT_ID);
        return redirectView;
    }

    @GetMapping("/callback0")
    public String home0(HttpServletRequest request) throws IOException {
        String code = request.getParameter("code");

        String url = "https://github.com/login/oauth/access_token";

        Map<String,Object> params = new LinkedHashMap<>();
        params.put("code", code);
        params.put("client_id", CLIENT_ID);
        params.put("client_secret", CLIENT_SECRET);

        String token = myApiCall(params, url, "POST").split("&", 2)[0].split("=", 2)[1];
        Map<String,Object> params2 = new LinkedHashMap<>();

        String res = myApiCall(params2, "https://api.github.com/user?access_token=" + token, "GET" );
        login = res.split(",", 2)[0].split(":", 2)[1].replace("\"", "");
        String id = res.split(",", 3)[1].split(":", 2)[1];
        System.out.println(login);

        return "/uploadeds/home";
    }

    @GetMapping("/uploadeds")
    public String index(Model model) {
        List<Uploaded> uploadeds = uploadedService.findAll();
        model.addAttribute("uploadeds", uploadeds);
        return "uploadeds/index";
    }

    @GetMapping("/uploadeds/search")
    public String search_and_see_screen(Model model) {
      return "uploadeds/search_and_see";
    }

    @PostMapping("/uploadeds/search")
    public String search(Model model, @RequestParam String query) {
      List<Uploaded> uploadeds = uploadedRepository.findUploadedByName(query);
      model.addAttribute("uploadeds", uploadeds);
      return "uploadeds/search_and_see";
    }

    @GetMapping("/uploadeds/new")
    public String newuploaded(Model model) {
        return "uploadeds/new";
    }

    @GetMapping("/uploadeds/{id}/edit")
    public String edit(@PathVariable Long id, Model model) { 
        Uploaded uploaded = uploadedService.findOne(id);
        model.addAttribute("uploaded", uploaded);
        return "uploadeds/edit";
    }

    @GetMapping("/uploadeds/{id}")
    public String show(@PathVariable Long id, Model model) {
        Uploaded uploaded = uploadedService.findOne(id);
        model.addAttribute("uploaded", uploaded);
        return "uploadeds/show";
    }

    @PostMapping("/uploadeds/new")
    public String create(@ModelAttribute Uploaded uploaded) {
        uploaded.setOwner(login);
        uploadedService.save(uploaded);
        return "redirect:/uploadeds";
    }

    @PostMapping("/uploadeds/{id}")
    public String done(@PathVariable Long id) {
        Uploaded uploaded = uploadedService.findOne(id);
        uploaded.setDone();
        uploadedService.save(uploaded);
        return "redirect:/uploadeds";
    }

    @PutMapping("/uploadeds/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Uploaded uploaded) {
        uploaded.setId(id);
        uploadedService.save(uploaded);
        return "redirect:/uploadeds";
    }

    @DeleteMapping("/uploadeds/{id}")
    public String destroy(@PathVariable Long id) {
        uploadedService.delete(id);
        return "redirect:/uploadeds";
    }
    
}
