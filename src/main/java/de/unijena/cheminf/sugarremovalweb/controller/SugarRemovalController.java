/*
 * MIT License
 *
 * Copyright (c) 2023 Jonas Schaub, Achim Zielesny, Christoph Steinbeck, Maria Sorokina
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.unijena.cheminf.sugarremovalweb.controller;

import de.unijena.cheminf.sugarremovalweb.misc.SessionCleaner;
import de.unijena.cheminf.sugarremovalweb.model.ProcessedMolecule;
import de.unijena.cheminf.sugarremovalweb.model.SubmittedMoleculeData;
import de.unijena.cheminf.sugarremovalweb.readers.ReaderService;
import de.unijena.cheminf.sugarremovalweb.readers.UserInputMoleculeReaderService;
import de.unijena.cheminf.sugarremovalweb.services.SugarRemovalService;
import de.unijena.cheminf.sugarremovalweb.storage.StorageFileNotFoundException;
import de.unijena.cheminf.sugarremovalweb.storage.StorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

/**
 * @author mSorok
 */
@RestController
@RequestMapping("molecule")
public class SugarRemovalController {

    @Autowired
    HttpServletRequest request;

    private final StorageService storageService;

    public ArrayList<String> sugarRemovalParameters;


    @Autowired
    ReaderService readerService;

    @Autowired
    SessionCleaner sessionCleaner;

    @Autowired
    SugarRemovalService sugarRemovalService;


    @Autowired
    UserInputMoleculeReaderService userInputMoleculeReaderService;

    @Autowired
    public SugarRemovalController(StorageService storageService) {
        this.storageService = storageService;
    }

    ArrayList<ProcessedMolecule> processedMolecules ;


    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ArrayList<ProcessedMolecule>> catchMoleculeAndParameters(@RequestBody SubmittedMoleculeData submittedMoleculeData){

        //System.out.println(submittedMoleculeData.getDataString());
        //System.out.println(submittedMoleculeData.getSugarsToRemove());

        processedMolecules = sugarRemovalService.doWork(submittedMoleculeData);

        if(processedMolecules.isEmpty()){
            return new ResponseEntity(processedMolecules, HttpStatus.BAD_REQUEST);
        }


        return new ResponseEntity(processedMolecules, HttpStatus.OK);
    }



    @PostMapping(consumes = { "multipart/form-data" }, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ArrayList<ProcessedMolecule>>  catchUploadedFileAndParameters(@RequestPart("submittedMoleculeData") SubmittedMoleculeData submittedMoleculeData,
                       @RequestPart("file") MultipartFile file) {

        //System.out.println(submittedMoleculeData.getDataString());
        //System.out.println(submittedMoleculeData.getSugarsToRemove());


        if(!file.isEmpty()) {
            storageService.store(file);
            String loadedFile = "upload-dir/" + file.getOriginalFilename();
            //System.out.println(loadedFile);



            processedMolecules = sugarRemovalService.doWork(submittedMoleculeData, loadedFile);
            //System.out.println(processedMolecules);

            if(processedMolecules.isEmpty()){
                return new ResponseEntity(processedMolecules, HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity(processedMolecules, HttpStatus.OK);
        }

        return new ResponseEntity(processedMolecules, HttpStatus.BAD_REQUEST);
    }



    @PutMapping
    public String updateMoleculeAndParameters(){
        return "update function called";
    }

    @DeleteMapping
    public String deleteMoleculeAndParameters(){
        return "deleted molecules called";
    }







/******** FILE HANDLING ********/




        /**
         * Handles file not found and file not uploaded situations
         * @param exc
         * @return
         */
    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    /**
     *
     * @param filename
     * @return
     *
     * from page / when files have been submitted, serves the files (loads)
     */
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

}
