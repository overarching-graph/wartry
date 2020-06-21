package com.hp.file_upload;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


import controller.FileUploadController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@Configuration
@EnableAutoConfiguration
@ComponentScan({"file_upload","controller"})

public class GraphFileUploadApplication extends SpringBootServletInitializer {
	 @Override
	  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder){
	    return builder.sources(GraphFileUploadApplication.class);
	 }
	  
	public static void main(String[] args) {
		
		new File(FileUploadController.uploadDirectory).mkdir();
		
		SpringApplication.run(GraphFileUploadApplication.class, args);
	}

	

}




