/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.restservice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

import com.example.exceptions.AppException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CityControllerTests {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void validateRegisterCity() throws Exception {
		Map<String, String> req = new HashMap<>();
		req.put("city", "chennai");	
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(req);

		// register new city 1
    	mockMvc.perform(post("/registerCity")
			.contentType(MediaType.APPLICATION_JSON)
			.content(json)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value("registered"));
		
		// register duplicate city
		mockMvc.perform(post("/registerCity")
			.contentType(MediaType.APPLICATION_JSON)
			.content(json)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value("already registered in system"));

		req.put("city", "ncr");	
		json = mapper.writeValueAsString(req);
		// register new city 2
    	mockMvc.perform(post("/registerCity")
			.contentType(MediaType.APPLICATION_JSON)
			.content(json)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value("registered"));
	}

}
