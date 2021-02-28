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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;

import java.util.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class AppFlowTest {

	@Autowired
	private MockMvc mockMvc;
	
	//// @Autowired
	// private ObjectMapper mapper;

		@Test
		@Order(1)
		public void validateAppFlow() throws Exception {
			Map<String, Object> req = new HashMap<>();
			req.put("city", "newcity1");	
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(req);

			// register new city1
			mockMvc.perform(post("/registerCity")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
			
			
			req.put("city", "newcity2");	
			json = mapper.writeValueAsString(req);

			// register new city2
			mockMvc.perform(post("/registerCity")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
			
			req.put("city", "newcity3");	
			json = mapper.writeValueAsString(req);

			// register new city3
			mockMvc.perform(post("/registerCity")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());


			req = new HashMap<>();
			req.put("cityId", 1	);	
			req.put("driverName", "qwerty");	
			req.put("cabNumber", "CH234 456");	
			req.put("lisenceId", "23456");	
			json = mapper.writeValueAsString(req);

			// register new cab 1 city 1
			mockMvc.perform(post("/registerCab")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("registered"));

			req = new HashMap<>();
			req.put("cityId", 1	);	
			req.put("driverName", "qwerty");	
			req.put("cabNumber", "PB234 456");	
			req.put("lisenceId", "23456");	
			json = mapper.writeValueAsString(req);

			// register new cab 2 city 2
			mockMvc.perform(post("/registerCab")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("registered"));

			// register new cab 3 city 2
			mockMvc.perform(post("/registerCab")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("registered"));

			req = new HashMap<>();
			req.put("cabId", 1	);	
			req.put("duty", "OFF");
			json = mapper.writeValueAsString(req);
			mockMvc.perform(put("/updateCabDuty")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

			req = new HashMap<>();
			req.put("cabId", 1	);
			req.put("duty", "ON");
			json = mapper.writeValueAsString(req);
			mockMvc.perform(put("/updateCabDuty")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
			
		}

}
