package org.nd.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.nd.models.Css;
import org.nd.services.ProcessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@Controller
public class InterfaceController {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ProcessorService processorService;
	
	private ObjectMapper jsonMapper = new ObjectMapper();
	
	@RequestMapping("/")
	public String welcome(Map<String, Object> model) {
		return "application";
	}	
	
	@PostMapping("/upload") 
    public String fileEndpoint(Map<String, Object> model, @RequestParam("files") MultipartFile[] files) {
		
		log.debug("files received");
		
		List<Css> list = new ArrayList<Css>();
		
		for(MultipartFile file : files) {
			
			try {
				byte[] bytes = file.getBytes();
				String content  =  new String(bytes);
				list.add(new Css(content, file.getOriginalFilename()));
			} catch (IOException e) {}
			
		}
		
		
		List<Css> convertedCss = processorService.process(list);
		

		for(Css converted : convertedCss) {
			try {
				String newName  = converted.getFilename().replace(".", "_ar.");
				converted.setFilename(newName);
				converted.setOriginal(null);			

			} catch (Exception e) {}
			
		}
		
		String response = "";
		try {
			response = jsonMapper.writeValueAsString(convertedCss);
		} catch (JsonProcessingException e) {}
  
	    model.put("response", response);
	    
	    return "json";

    }
	
	@PostMapping("/text") 
    public String textEndpoint(Map<String, Object> model, @RequestBody String text) {
		
		log.debug("text received");
		
		List<Css> list = new ArrayList<Css>();
		list.add(new Css(text, null)); 

		List<Css> responses = processorService.process(list);
		
		String response = ""; 

		try {
			response = Base64.getEncoder().encodeToString(responses.get(0).getConverted().getBytes());
		} catch (Exception e) {}
		
	    model.put("response", response);
	    
	    return "json";
    }
	
	
}