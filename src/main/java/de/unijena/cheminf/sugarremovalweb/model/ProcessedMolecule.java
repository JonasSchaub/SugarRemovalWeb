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

package de.unijena.cheminf.sugarremovalweb.model;

import org.openscience.cdk.interfaces.IAtomContainer;

import java.util.ArrayList;

/**
 * Data model for processed molecules, i.e. aglycones returned by the SRU.
 *
 * @author Maria Sorokina (https://github.com/mSorok)
 */
public class ProcessedMolecule {
    //<editor-fold desc="Private class variables">
    /**
     * Array of keywords describing which type of sugars to remove and some other SRU settings.
     */
    private ArrayList<String> sugarRemovalSettingsKeywords;
    //
    /**
     * Atom container for input structure.
     */
    private IAtomContainer molecule;
    //
    /**
     * SMILES code of the input structure.
     */
    private String smiles;
    //
    /**
     * SMILES code of the input structure that has been adapted for GUI display, e.g. line breaks can be introduced.
     */
    private String displaySmiles;
    //
    /**
     * Whether a sugar was detected and removed from the input molecule or not. Defaults to false.
     */
    private boolean sugarWasRemoved = false;
    //
    /**
     * Sugar moiety structures that were removed.
     */
    private ArrayList<IAtomContainer> sugarMoietiesRemoved;
    //
    /**
     * SMILES representations of the sugar moiety structures that were removed.
     * TODO: are they properly synchronised? Maybe combine them in some way!
     */
    private ArrayList<String> sugarMoietiesRemovedSmiles;
    //
    /**
     * List of aglycone structures.
     * TODO: Why a list??
     */
    private ArrayList<IAtomContainer> deglycosylatedMoieties;
    //
    /**
     * SMILES representations of aglycone structures.
     */
    private ArrayList<String> deglycosylatedMoietiesSmiles;
    //
    /**
     * Input file format the structure was aprsed from.
     * TODO: Replace with enum value?
     */
    private String submittedDataType;
    //
    /**
     * InChI key (TODO: of what??).
     */
    private String inchikey;
    //</editor-fold>
    //
    //<editor-fold desc="Constructor">
    /**
     *
     */
    public ProcessedMolecule() {
        this.sugarRemovalSettingsKeywords = new ArrayList<>();
        this.deglycosylatedMoieties = new ArrayList<>();
        this.deglycosylatedMoietiesSmiles = new ArrayList<>();
        this.sugarMoietiesRemoved = new ArrayList<>();
        this.sugarMoietiesRemovedSmiles = new ArrayList<>();
    }
    //</editor-fold>
    //

    //<editor-fold desc="Public properties (get and set)">
    /**
     *
     * @return
     */
    public ArrayList<String> getSugarRemovalSettingsKeywords() {
        return this.sugarRemovalSettingsKeywords;
    }
    //
    /**
     *
     * @param sugarRemovalSettingsKeywords
     */
    public void setSugarRemovalSettingsKeywords(ArrayList<String> sugarRemovalSettingsKeywords) {
        this.sugarRemovalSettingsKeywords = sugarRemovalSettingsKeywords;
    }
    //
    /**
     *
     * @param aKeyword
     */
    public void addSugarRemovalSettingsKeywords(String aKeyword) {
        this.sugarRemovalSettingsKeywords.add(aKeyword);
    }
    /**
     *
     * @return
     */
    public IAtomContainer getMolecule() {
        return this.molecule;
    }
    //
    /**
     *
     * @param molecule
     */
    public void setMolecule(IAtomContainer molecule) {
        this.molecule = molecule;
    }
    //
    /**
     *
     * @return
     */
    public String getSmiles() {
        return this.smiles;
    }
    //
    /**
     *
     * @param smiles
     */
    public void setSmiles(String smiles) {
        this.smiles = smiles;
    }
    //
    /**
     *
     * @return
     */
    public ArrayList<IAtomContainer> getSugarMoietiesRemoved() {
        return this.sugarMoietiesRemoved;
    }
    //
    /**
     *
     * @param sugarMoietiesRemoved
     */
    public void setSugarMoietiesRemoved(ArrayList<IAtomContainer> sugarMoietiesRemoved) {
        this.sugarMoietiesRemoved = sugarMoietiesRemoved;
    }
    //
    /**
     *
     */
    public void addSugarMoietiesRemoved(IAtomContainer aRemovedSugarMoiety) {
        this.sugarMoietiesRemoved.add(aRemovedSugarMoiety);
    }
    //
    /**
     *
     * @return
     */
    public ArrayList<IAtomContainer> getDeglycosylatedMoieties() {
        return this.deglycosylatedMoieties;
    }
    //
    /**
     *
     * @param deglycosylatedMoieties
     */
    public void setDeglycosylatedMoieties(ArrayList<IAtomContainer> deglycosylatedMoieties) {
        this.deglycosylatedMoieties = deglycosylatedMoieties;
    }
    //
    /**
     *
     */
    public void addDeglycosylatedMoieties(IAtomContainer aDeglycosylatedMoiety) {
        this.deglycosylatedMoieties.add(aDeglycosylatedMoiety);
    }
    //
    /**
     *
     * @return
     */
    public String getSubmittedDataType() {
        return this.submittedDataType;
    }
    //
    /**
     *
     * @param submittedDataType
     */
    public void setSubmittedDataType(String submittedDataType) {
        this.submittedDataType = submittedDataType;
    }
    //
    /**
     *
     * @return
     */
    public ArrayList<String> getSugarMoietiesRemovedSmiles() {
        return this.sugarMoietiesRemovedSmiles;
    }
    //
    /**
     *
     * @param sugarMoietiesRemovedSmiles
     */
    public void setSugarMoietiesRemovedSmiles(ArrayList<String> sugarMoietiesRemovedSmiles) {
        this.sugarMoietiesRemovedSmiles = sugarMoietiesRemovedSmiles;
    }
    //
    /**
     *
     */
    public void addSugarMoietiesRemovedSmiles(String aSugarMoietiesRemovedSmiles) {
        this.sugarMoietiesRemovedSmiles.add(aSugarMoietiesRemovedSmiles);
    }
    //
    /**
     *
     * @return
     */
    public ArrayList<String> getDeglycosylatedMoietiesSmiles() {
        return this.deglycosylatedMoietiesSmiles;
    }
    //
    /**
     *
     * @param deglycosylatedMoietiesSmiles
     */
    public void setDeglycosylatedMoietiesSmiles(ArrayList<String> deglycosylatedMoietiesSmiles) {
        this.deglycosylatedMoietiesSmiles = deglycosylatedMoietiesSmiles;
    }
    //
    /**
     *
     */
    public void addDeglycosylatedMoietiesSmiles(String aDeglycosylatedMoietiesSmiles) {
        this.deglycosylatedMoietiesSmiles.add(aDeglycosylatedMoietiesSmiles);
    }
    //
    /**
     *
     * @return
     */
    public String getInchikey() {
        return this.inchikey;
    }
    //
    /**
     *
     * @param inchikey
     */
    public void setInchikey(String inchikey) {
        this.inchikey = inchikey;
    }
    //
    /**
     *
     * @return
     */
    public String getDisplaySmiles() {
        return this.displaySmiles;
    }
    //
    /**
     *
     * @param displaySmiles
     */
    public void setDisplaySmiles(String displaySmiles) {
        this.displaySmiles = displaySmiles;
    }
    //
    /**
     *
     * @return
     */
    public boolean isSugarWasRemoved() {
        return this.sugarWasRemoved;
    }
    //
    /**
     *
     * @param sugarWasRemoved
     */
    public void setSugarWasRemoved(boolean sugarWasRemoved) {
        this.sugarWasRemoved = sugarWasRemoved;
    }
    //</editor-fold>
}
