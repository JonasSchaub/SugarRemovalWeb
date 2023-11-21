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

import jakarta.servlet.http.HttpServletRequest;

/**
 * Backend controller of the application, handling requests.
 *
 * @author Maria Sorokina (https://github.com/mSorok)
 * @author Jonas Schaub (https://github.com/JonasSchaub)
 */
@RestController
@RequestMapping("molecule")
public class SugarRemovalController {
    //TODO: Remove seemingly unused variables?
    //<editor-fold desc="Private Autowired Spring beans">
    /**
     * The HTTP user request that is given.
     */
    @Autowired
    private HttpServletRequest request;
    //
    /**
     * Service to read submitted molecule files.
     */
    @Autowired
    private ReaderService readerService;
    //
    /**
     * Session cleaner. TODO: this does not do anything!
     */
    @Autowired
    private SessionCleaner sessionCleaner;
    //
    /**
     * Service class for deglycosylating molecules.
     */
    @Autowired
    private SugarRemovalService sugarRemovalService;
    //
    /**
     * Service for transformation of submitted data formats.
     */
    @Autowired
    private UserInputMoleculeReaderService userInputMoleculeReaderService;
    //</editor-fold>
    //
    //<editor-fold desc="Private final class fields">
    /**
     * Storage service for file handling.
     */
    private final StorageService storageService;
    //</editor-fold>
    //
    //<editor-fold desc="Private class fields">
    /**
     * List of submitted sugar removal parameters.
     */
    private ArrayList<String> sugarRemovalParameters;
    //
    /**
     * Cache for return value.
     */
    private ArrayList<ProcessedMolecule> processedMolecules;
    //</editor-fold>
    //
    //<editor-fold desc="Constructor">
    /**
     * Sole constructor that requires a storage service parameter for file handling.
     *
     * @param storageService used for FileHandling
     */
    @Autowired
    public SugarRemovalController(StorageService storageService) {
        this.storageService = storageService;
    }
    //</editor-fold>
    //
    //<editor-fold desc="Public HTTP methods">
    /**
     * Performs deglycosylation on the submitted molecules and returns aglycones. Used for molecules
     * submitted as SMILES codes, not as file
     *
     * @param submittedMoleculeData parsed input molecules and parameters
     * @return response entity containing list of processed molecules
     */
    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<ArrayList<ProcessedMolecule>> catchMoleculeAndParameters(
            @RequestBody SubmittedMoleculeData submittedMoleculeData){
        this.processedMolecules = this.sugarRemovalService.doWork(submittedMoleculeData);
        if(this.processedMolecules.isEmpty()){
            return new ResponseEntity(this.processedMolecules, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(this.processedMolecules, HttpStatus.OK);
    }
    //
    /**
     * Performs deglycosylation on the submitted molecules and returns aglycones. Used for molecules
     * submitted as uploaded files.
     *
     * @param submittedMoleculeData parsed input molecules and parameters
     * @param file submitted input file
     * @return response entity containing list of processed molecules
     */
    @PostMapping(
            consumes = { "multipart/form-data" },
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<ArrayList<ProcessedMolecule>>  catchUploadedFileAndParameters(
            @RequestPart("submittedMoleculeData") SubmittedMoleculeData submittedMoleculeData,
            @RequestPart("file") MultipartFile file) {
        if(file.isEmpty()) {
            return new ResponseEntity(processedMolecules, HttpStatus.BAD_REQUEST);
        }
        this.storageService.store(file);
        String loadedFile = String.valueOf(this.storageService.load(file.getOriginalFilename()));
        this.processedMolecules = this.sugarRemovalService.doWork(submittedMoleculeData, loadedFile);
        if(this.processedMolecules.isEmpty()){
            return new ResponseEntity(processedMolecules, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(processedMolecules, HttpStatus.OK);
    }
    //
    //TODO: are these methods needed? I guess they are placeholders for put and delete requests; implement functionality?
    /**
     * Placeholder PUT method.
     *
     * @return info string
     */
    @PutMapping
    public String updateMoleculeAndParameters(){
        return "Update function called";
    }
    //
    /**
     * Placeholder DELETE method.
     *
     * @return info string
     */
    @DeleteMapping
    public String deleteMoleculeAndParameters(){
        return "Delete molecules called";
    }
    //
    /**
     * Handles file not found and file not uploaded situations.
     *
     * @param exc StorageFileNotFoundException
     * @return "not found" response entity
     */
    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
    //
    /**
     * from page / when files have been submitted, serves the files (loads).
     *
     * @param filename name of the file to load
     * @return response entity with imported file as body
     */
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = this.storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
    //</editor-fold>
}
