package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.provider.Connection;

@Controller
public class AppController {
	
	
	@GetMapping("/")
	public String index() {
		
		Connection.consumeLogin();
		
		return "index";
	}
}
