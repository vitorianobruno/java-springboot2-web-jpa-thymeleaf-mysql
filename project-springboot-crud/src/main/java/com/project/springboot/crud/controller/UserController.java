package com.project.springboot.crud.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.validation.Valid;

import com.project.springboot.crud.model.User;
import com.project.springboot.crud.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
	
	UserRepository userRepository;

	@Autowired
	public UserController(UserRepository userRepository) {
	    this.userRepository = userRepository;
	}
	
	@GetMapping("/")
	public String getIndex() {
		return "redirect:/index";
	}
	
	@GetMapping("/index")
	public String index(Model model) {
		//Get current date time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formatDateTime = now.format(formatter);
        
		model.addAttribute("tstamp", formatDateTime);
		model.addAttribute("users", userRepository.findAll());
		return "index";
	}
	
	@GetMapping("/signup")
	public String showSignUpForm(User user) {
		return "add-user";
	}

	@PostMapping("/adduser")
	public String addUser(@Valid User user, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "add-user";
		}

		userRepository.save(user);
		model.addAttribute("users", userRepository.findAll());
		return "redirect:/index";
	}

	@GetMapping("/edit/{id}")
	public String showUpdateForm(@PathVariable("id") long id, Model model) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

		model.addAttribute("user", user);
		return "update-user";
	}

	@PostMapping("/update/{id}")
	public String updateUser(@PathVariable("id") long id, @Valid User user, BindingResult result, Model model) {
		if (result.hasErrors()) {
			user.setId(id);
			return "update-user";
		}

		userRepository.save(user);
		model.addAttribute("users", userRepository.findAll());
		return "redirect:/index";
	}

	@GetMapping("/delete/{id}")
	public String deleteUser(@PathVariable("id") long id, Model model) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		userRepository.delete(user);
		model.addAttribute("users", userRepository.findAll());
		return "index";
	}

}
