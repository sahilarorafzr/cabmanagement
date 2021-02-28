package com.example.restservice;

import com.example.pojo.Greeting;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

	@GetMapping("/healthCheck")
	public Greeting greeting() {
		return new Greeting("Hello World");
	}
}
