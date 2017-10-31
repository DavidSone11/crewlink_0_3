package com.mathologic.projects.crewlink.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.mathologic.projects.crewlink.utils.converters.CsvToDatabase;
import com.opencsv.CSVReader;

/**
 * Created by vivek on 31/10/15.
 */
@Controller
public class FileUploads {
    @Autowired
    @Qualifier("TrainTimeTable")
    CsvToDatabase trainTimeTableCsvToDatabase;
    
    @Autowired
    @Qualifier("TrainDetails")
    CsvToDatabase trainDetailsCsvToDatabase;
    
    
    @Autowired
    @Qualifier("StationDetails")
    CsvToDatabase stationDetailsCsvToDatabase;

    @RequestMapping(value="/upload", method= RequestMethod.GET)
    public @ResponseBody String provideUploadInfo() {
        return "You can upload a file by posting to this same URL.";
    }
    
    /**
     * 
     * @param fileTrainDetails ( eg. train_details.csv)
     * @param request ( eg. POST )
     * @return ( eg. uploaded successfully or Unable to upload)
     */
    @RequestMapping(value="/uploadTrainDetails", method=RequestMethod.POST)
    public @ResponseBody String handleUploadTrainDetails(@RequestParam("file") MultipartFile fileTrainDetails,HttpServletRequest request){
    // creates location to upload csv file
   	 //String nameTrainDetails = this.getClass().getClassLoader().getResource("").getPath() 
   	Path currentRelativePath = Paths.get("");
   	String s = currentRelativePath.toAbsolutePath().toString();
   	String nameTrainDetails = s+"/uploads";
     
     
     //check csv file is not empty
     if (!fileTrainDetails.isEmpty()) {
         try {
        	 // writes to the buffer contents of the csv file
        	 File file = new File(nameTrainDetails);
        	 file.mkdirs();
        	 nameTrainDetails += "/"+LocalDateTime.now().toString().replace(':','-')+fileTrainDetails.getOriginalFilename();
        	 nameTrainDetails = nameTrainDetails.replace("%20", " ");
        	 nameTrainDetails = nameTrainDetails.replace("\\", "/");
        	 file =  new File(nameTrainDetails);
             byte[] bytes = fileTrainDetails.getBytes();
             BufferedOutputStream stream =
                     new BufferedOutputStream(new FileOutputStream(file));
             stream.write(bytes);
             stream.close();

             //If file is not type of csv type means do not allow to upload
             if(!fileTrainDetails.getOriginalFilename().contains(".csv")){
                 return "Uploaded file is not CSV";
             }

            
             CSVReader reader = new CSVReader(new FileReader(nameTrainDetails),',','\'',1);
             for (String[] line; (line = reader.readNext()) != null;) {
             	trainDetailsCsvToDatabase.processRecord(line);
             }

             return "You successfully uploaded " + nameTrainDetails + "!";
         } catch (Exception e) {
             return "You failed to upload " + nameTrainDetails + " => " + e.getMessage();
         }
     } else {
         return "You failed to upload " + nameTrainDetails + " because the file was empty.";
     }
    	
   
    }

    /**
     * 
     * @param file ( eg. train_runnning_details.csv)
     * @param request ( eg. POST)
     * @return ( eg. success or failure)
     */
    @RequestMapping(value="/uploadTrainTimeTable", method=RequestMethod.POST)
    public @ResponseBody String handleUploadTrainTimeTable(@RequestParam("file") MultipartFile file, HttpServletRequest request){
        // create the location to upload the file
    	Path currentRelativePath = Paths.get("");
       	String s = currentRelativePath.toAbsolutePath().toString();
       	String name = s+"/uploads";
         
        //check file should not be empty
        if (!file.isEmpty()) {
            try {
            	// get the bytes of the file
                byte[] bytes = file.getBytes();
                File outFile = new File(name);
                outFile.mkdirs();
                name += "/"+LocalDateTime.now().toString().replace(':','-')+file.getOriginalFilename();
                name = name.replace("%20", " ");
                name = name.replace("\\", "/");
	           	outFile =  new File(name);
                // create buffer to write
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(outFile));
                stream.write(bytes);
                stream.close();

                // check it should be csv type file
                if(!file.getOriginalFilename().contains(".csv")){
                    return "Uploaded file is not CSV";
                }

                //BufferedReader buffReader = new BufferedReader(new InputStreamReader(request.getInputStream()));
                // create csvreader object to read the csv file line by line
                CSVReader reader = new CSVReader(new FileReader(name),',','\'',1);
                for (String[] line; (line = reader.readNext()) != null;) {
                	// send each read line to the train details csv process record method
                	trainTimeTableCsvToDatabase.processRecord(line);
                }
                String [] line = {"END"};
                trainTimeTableCsvToDatabase.processRecord(line);

                return "You successfully uploaded " + name + "!";
            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + name + " because the file was empty.";
        }
    }
   /* 
    @RequestMapping(value="/uploadTrainDetails", method=RequestMethod.POST)
    public @ResponseBody String handleUploadTrainDetails(@RequestParam("file") MultipartFile fileTrainDetails, 
    		@RequestParam("file") MultipartFile fileTrainStartDay,
    		HttpServletRequest request){
    	 String nameTrainDetails = this.getClass().getClassLoader().getResource("").getPath() +"uploads/"+LocalDateTime.now().toString().replace(':','-')+fileTrainDetails.getOriginalFilename();
         String nameTrainStartDay = this.getClass().getClassLoader().getResource("").getPath() +"uploads/"+LocalDateTime.now().toString().replace(':','-')+fileTrainStartDay.getOriginalFilename();
         System.out.println("file Path :"+nameTrainDetails);
         System.out.println("file Path :"+nameTrainStartDay);
         nameTrainDetails = nameTrainDetails.replace("%20", " ");
         nameTrainStartDay = nameTrainStartDay.replace("%20", " ");
         
         System.out.println(nameTrainDetails+" \t\t " +nameTrainStartDay);

        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(name)));
                stream.write(bytes);
                stream.close();

                if(!file.getOriginalFilename().contains(".csv")){
                    return "Uploaded file is not CSV";
                }

                //BufferedReader buffReader = new BufferedReader(new InputStreamReader(request.getInputStream()));
                CSVReader reader = new CSVReader(new FileReader(name),',','\'',1);
                for (String[] line; (line = reader.readNext()) != null;) {
                	trainDetailsCsvToDatabase.processRecord(line);
                }

                return "You successfully uploaded " + name + "!";
            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + name + " because the file was empty.";
        }
        
        return null;
    }*/
    
    /*
    @RequestMapping(value="/uploadTrainDetailsStartDay", method=RequestMethod.POST)
    public @ResponseBody String handleUploadTrainDetailsStartDay(@RequestParam("file") MultipartFile fileTrainDetails,HttpServletRequest request){
    	
   	 String nameTrainDetails = this.getClass().getClassLoader().getResource("").getPath() +"uploads/"+LocalDateTime.now().toString().replace(':','-')+fileTrainDetails.getOriginalFilename();
     nameTrainDetails = nameTrainDetails.replace("%20", " ");
     
     System.out.println("Filename " +fileTrainDetails);
     if (!fileTrainDetails.isEmpty()) {
         try {
             byte[] bytes = fileTrainDetails.getBytes();
             BufferedOutputStream stream =
                     new BufferedOutputStream(new FileOutputStream(new File(nameTrainDetails)));
             stream.write(bytes);
             stream.close();

             if(!fileTrainDetails.getOriginalFilename().contains(".csv")){
                 return "Uploaded file is not CSV";
             }

             CSVReader reader = new CSVReader(new FileReader(nameTrainDetails),',','\'',1);
             for (String[] line; (line = reader.readNext()) != null;) {
             	trainDetailsCsvToDatabase.processRecordSartDay(line);
             }

             return "You successfully uploaded " + nameTrainDetails + "!";
         } catch (Exception e) {
             return "You failed to upload " + nameTrainDetails + " => " + e.getMessage();
         }
     } else {
         return "You failed to upload " + nameTrainDetails + " because the file was empty.";
     }
    	
   
    }
    
*/    
    
    
    
    /*
    
    
    @RequestMapping(value="/uploadStationDetails", method=RequestMethod.POST)
    public @ResponseBody String handleUploadStationDetails(@RequestParam("file") MultipartFile fileTrainDetails,HttpServletRequest request){
    	
   	 String nameTrainDetails = this.getClass().getClassLoader().getResource("").getPath() +"uploads/"+LocalDateTime.now().toString().replace(':','-')+fileTrainDetails.getOriginalFilename();
     nameTrainDetails = nameTrainDetails.replace("%20", " ");
     
     System.out.println("Filename " +fileTrainDetails);
     if (!fileTrainDetails.isEmpty()) {
         try {
             byte[] bytes = fileTrainDetails.getBytes();
             BufferedOutputStream stream =
                     new BufferedOutputStream(new FileOutputStream(new File(nameTrainDetails)));
             stream.write(bytes);
             stream.close();

             if(!fileTrainDetails.getOriginalFilename().contains(".csv")){
                 return "Uploaded file is not CSV";
             }

             CSVReader reader = new CSVReader(new FileReader(nameTrainDetails),',','\'',1);
             for (String[] line; (line = reader.readNext()) != null;) {
             	stationDetailsCsvToDatabase.processRecord(line);
             }

             return "You successfully uploaded " + nameTrainDetails + "!";
         } catch (Exception e) {
             return "You failed to upload " + nameTrainDetails + " => " + e.getMessage();
         }
     } else {
         return "You failed to upload " + nameTrainDetails + " because the file was empty.";
     }
    	
   
    }
    */
    
   
}
