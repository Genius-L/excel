package com.example.demo.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExcelController {

    private String production = "false";

    @RequestMapping(value = "/hello")
	public String helloSpring() {
		return "Hello, from excel controller ...";
	}

    @RequestMapping(value = "/csv")
	public ResponseEntity<InputStreamResource> downloadCsvFile() throws IOException {
        //data
        String[][] employees = {
            {"Man", "Sparkes", "msparkes0@springhow.com", "Engineering"},
            {"Dulcinea", "Terzi", "dterzi1@springhow.com", "Engineering"},
            {"Tamar", "Bedder", "tbedder2@springhow.com", "Legal"},
            {"Vance", "Scouller", "vscouller3@springhow.com", "Sales"},
            {"Gran", "Jagoe", "gjagoe4@springhow.com", "Business Development"}
        };
        // path 
         // Create a unique filename or use the original filename
        String fileName = "employees.csv"; // You can make this dynamic

        // Create the file in the configured directory
        File file = new File(getSourcePath() + File.separator + fileName);
        
        //create a CSV printer
        CSVPrinter printer = new CSVPrinter(new FileWriter(file), CSVFormat.DEFAULT.withDelimiter(';'));
        //create header row
        printer.printRecord("FirstName", "LastName", "Email", "Department");
        // create data rows
        for (String[] employee : employees) {
            printer.printRecord(employee);
        }
        //close the printer after the file is complete
        printer.flush();
        printer.close();

		HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

        ClassPathResource pathResource = new ClassPathResource(fileName);
        File csvFile = pathResource.getFile();
        InputStreamResource resource = new InputStreamResource(new FileInputStream(csvFile));

        return ResponseEntity.ok()
        .headers(headers)
        .contentLength(csvFile.length())
        .contentType(MediaType.parseMediaType("text/csv")) 
        .body(resource);
                
	}

    private String getSourcePath() {
        String path = System.getProperty("user.dir") + "/src/main/resources";
        if (production.equals("true"))
            path = System.getProperty("user.dir") + "/webapps/" + "applicationName" + "/WEB-INF/classes/rapport";
        return path;
    }
    
}
