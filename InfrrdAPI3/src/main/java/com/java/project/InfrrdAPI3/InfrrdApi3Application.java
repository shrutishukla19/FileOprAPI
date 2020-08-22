package com.java.project.InfrrdAPI3;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.Channels;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;

@SpringBootApplication
@RestController
public class InfrrdApi3Application {

	public static void main(String[] args) {
		SpringApplication.run(InfrrdApi3Application.class, args);
	}
	
@RequestMapping(value="/",method=RequestMethod.GET)
public ResponseEntity<Object> downloadFile() throws IOException 
{
	FileWriter filewriter=null;
	try {
		
	CSVData csv1=new CSVData();
	csv1.setId("1");
	csv1.setName("Joy");
	csv1.setNumber("7898763678");
	
	CSVData csv2=new CSVData();
	csv2.setId("2");
	csv2.setName("Happy");
	csv2.setNumber("6758837647");
	
	List<CSVData> csvDataList=new ArrayList<>();
	csvDataList.add(csv1);
	csvDataList.add(csv2);
	
	StringBuilder fileContent=new StringBuilder("ID, Name, Number\n");
	for(CSVData csv:csvDataList)
	{
		fileContent.append(csv.getId()).append(",").append(csv.getName()).append(",").append(csv.getNumber()).append("\n");
	}
	
	String filename="src/main/resources/csvdata.csv";
	
	filewriter=new FileWriter(filename);
	filewriter.write(fileContent.toString());
	filewriter.flush();
	
	File file=new File(filename);

        
	InputStreamResource resource=new InputStreamResource(new FileInputStream(file));
        
	HttpHeaders headers=new HttpHeaders();
	headers.add("Content-Disposition",String.format("attachment;filename=\"%s\"", file.getName()));
	headers.add("Cache-Control", "no-cache,no-store,must-revalidate");
	headers.add("Pragma", "no-cache");
	headers.add("Expires", "0");
	ResponseEntity<Object> responseEntity=ResponseEntity.ok().headers(headers).contentLength(file.length()).contentType(MediaType.parseMediaType("application/txt")).body(resource);
	
	return responseEntity;
	}catch(Exception e)
	{
		return new ResponseEntity<>("error occurred",HttpStatus.INTERNAL_SERVER_ERROR);
	}finally
	{
		if(filewriter!=null)
			filewriter.close();
	}
}

@RequestMapping(value="/",method=RequestMethod.DELETE)
public ResponseEntity<Object> deleteFile()
{
	String filename="src/main/resources/csvdata.csv";
	File file=new File(filename);
	if(file.delete()) 
    {
		return new ResponseEntity<>("Deletion Successful",HttpStatus.ACCEPTED); 
		
    } 
    else
    {
    	return new ResponseEntity<>("error occurred",HttpStatus.INTERNAL_SERVER_ERROR);
    } 
	
}

@RequestMapping(value="/",method=RequestMethod.POST)
public ResponseEntity<Object> copyFile()
{
	File source=new File("src/main/resources/csvdata.csv");
	File dest = new File("src/main/resources/csvdata2.csv");
    try {
		Files.copy(source.toPath(), dest.toPath(),StandardCopyOption.REPLACE_EXISTING);
		return new ResponseEntity<>("Copy Successful",HttpStatus.ACCEPTED);
	} catch (IOException e) {
		return new ResponseEntity<>("error occurred",HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
}

}
