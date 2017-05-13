package com.noteanalyzer.mvc.controller;
 
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.noteanalyzer.mvc.model.NoteModel;
import com.noteanalyzer.mvc.model.NoteSummaryModel;
import com.noteanalyzer.utility.NoteUtility;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;
 
@RestController
public class NoteRestController {
 
 
    
    
	  
	
    @RequestMapping(value="/templateDownload", method = RequestMethod.GET)
    public void downloadFile(HttpServletResponse response) throws IOException {
     
        File file = null;
         
        //if(type.equalsIgnoreCase("internal")){
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            file = new File(classloader.getResource("noteTemplate.csv").getFile());
        //}else{
          //  file = new File(EXTERNAL_FILE_PATH);
       // }
         
       /* if(!file.exists()){
            String errorMessage = "Sorry. The file you are looking for does not exist";
            System.out.println(errorMessage);
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
            outputStream.close();
            return;
        }
         */
        String mimeType= URLConnection.guessContentTypeFromName(file.getName());
        if(mimeType==null){
            System.out.println("mimetype is not detectable, will take default");
            mimeType = "application/octet-stream";
        }
         
        System.out.println("mimetype : "+mimeType);
         
        response.setContentType(mimeType);
         
        /* "Content-Disposition : inline" will show viewable types [like images/text/pdf/anything viewable by browser] right on browser 
            while others(zip e.g) will be directly downloaded [may provide save as popup, based on your browser setting.]*/
        //response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() +"\""));
 
         
        /* "Content-Disposition : attachment" will be directly download, may provide save as popup, based on your browser setting*/
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
         
        response.setContentLength((int)file.length());
 
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
 
        //Copy bytes from source to destination(outputstream in this example), closes both streams.
        FileCopyUtils.copy(inputStream, response.getOutputStream());
    }
    
    
  @RequestMapping(value = "/api/noteUpload", method = RequestMethod.POST)
    public ResponseEntity<List<NoteModel>> multiFileUpload(MultipartHttpServletRequest request,
            RedirectAttributes redirectAttributes) throws IOException {
 
	  Iterator<String> iterator = request.getFileNames();
      MultipartFile multipart = null;
      List<NoteModel> responseList =  new ArrayList<>();
      while (iterator.hasNext()) {
          multipart = request.getFile(iterator.next());
          //do something with the file.....
      }
	  System.out.println("Index controler for filr upload called>>"+multipart);
        if (multipart == null) {
            System.out.println("Empty uploaded file");
            return new ResponseEntity<List<NoteModel>>(responseList, HttpStatus.BAD_REQUEST);
        } else {
            System.out.println("Fetching files"+multipart);
 
                ColumnPositionMappingStrategy strat = new ColumnPositionMappingStrategy();
                strat.setType(NoteModel.class);
                String[] columns = new String[] {"typeOfNote", "address","property","dateOfNote","upb","rate","pdiPayment","tdiPayment","remainingTerm"}; // the fields to bind do in your JavaBean
                strat.setColumnMapping(columns);
                File file = NoteUtility.convert(multipart);
                CsvToBean csv = new CsvToBean();
                FileReader fr = new FileReader(file);
                CSVReader csvReader = new CSVReader(new FileReader(file));

                List list = csv.parse(strat, csvReader);
                for (Object object : list) {
                	NoteModel note = (NoteModel) object;
                	System.out.println(note);
                	responseList.add(note);
                }
            return new ResponseEntity<List<NoteModel>>(responseList, HttpStatus.OK);
        }
    }
 
  

  
  @RequestMapping(value = "/fetchAllNotes", method = RequestMethod.GET)
  public ResponseEntity<List<NoteSummaryModel>> listAllNotes() {
  	System.out.println("Inside Arvind listAllNotes");
      List<NoteSummaryModel> notesList = new ArrayList<>();
      NoteSummaryModel note1 = new NoteSummaryModel("http://cdn.flaticon.com/png/256/70689.png","ad","ad","adsad","adafeae","asfasd", "asda");
      NoteSummaryModel note2 = new NoteSummaryModel("http://cdn.flaticon.com/png/256/70689.png","ad","ad","adsad","adafeae","asfasd", "asda");
      notesList.add(note1);
      notesList.add(note2);
      
      if(notesList.isEmpty()){
          return new ResponseEntity<List<NoteSummaryModel>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
      }
      return new ResponseEntity<List<NoteSummaryModel>>(notesList, HttpStatus.OK);
  }

  
    

 
}