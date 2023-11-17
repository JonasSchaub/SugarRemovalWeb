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
 * @author Maria Sorokina (https://github.com/mSorok)
 */
public class ProcessedMolecule {

    public ArrayList<String> sugarsToRemove;

    IAtomContainer molecule;

    public String smiles;
    public String displaySmiles;

    public boolean sugarWasRemoved = false;

    ArrayList<IAtomContainer> sugarMoietiesRemoved;

    public ArrayList<String> sugarMoietiesRemovedSmiles;

    ArrayList<IAtomContainer> deglycosylatedMoieties;

    public ArrayList<String> deglycosylatedMoietiesSmiles;

    String submittedDataType;

    String inchikey;


    public ArrayList<String> getSugarsToRemove() {
        return sugarsToRemove;
    }

    public void setSugarsToRemove(ArrayList<String> sugarsToRemove) {
        this.sugarsToRemove = sugarsToRemove;
    }

    public IAtomContainer getMolecule() {
        return molecule;
    }

    public void setMolecule(IAtomContainer molecule) {
        this.molecule = molecule;
    }

    public String getSmiles() {
        return smiles;
    }

    public void setSmiles(String smiles) {
        this.smiles = smiles;
    }

    public ArrayList<IAtomContainer> getSugarMoietiesRemoved() {
        return sugarMoietiesRemoved;
    }

    public void setSugarMoietiesRemoved(ArrayList<IAtomContainer> sugarMoietiesRemoved) {
        this.sugarMoietiesRemoved = sugarMoietiesRemoved;
    }

    public ArrayList<IAtomContainer> getDeglycosylatedMoieties() {
        return deglycosylatedMoieties;
    }

    public void setDeglycosylatedMoieties(ArrayList<IAtomContainer> deglycosylatedMoieties) {
        this.deglycosylatedMoieties = deglycosylatedMoieties;
    }

    public String getSubmittedDataType() {
        return submittedDataType;
    }

    public void setSubmittedDataType(String submittedDataType) {
        this.submittedDataType = submittedDataType;
    }

    public ArrayList<String> getSugarMoietiesRemovedSmiles() {
        return sugarMoietiesRemovedSmiles;
    }

    public void setSugarMoietiesRemovedSmiles(ArrayList<String> sugarMoietiesRemovedSmiles) {
        this.sugarMoietiesRemovedSmiles = sugarMoietiesRemovedSmiles;
    }

    public ArrayList<String> getDeglycosylatedMoietiesSmiles() {
        return deglycosylatedMoietiesSmiles;
    }

    public void setDeglycosylatedMoietiesSmiles(ArrayList<String> deglycosylatedMoietiesSmiles) {
        this.deglycosylatedMoietiesSmiles = deglycosylatedMoietiesSmiles;
    }

    public String getInchikey() {
        return inchikey;
    }

    public void setInchikey(String inchikey) {
        this.inchikey = inchikey;
    }

    public String getDisplaySmiles() {
        return displaySmiles;
    }

    public void setDisplaySmiles(String displaySmiles) {
        this.displaySmiles = displaySmiles;
    }

    public boolean isSugarWasRemoved() {
        return sugarWasRemoved;
    }

    public void setSugarWasRemoved(boolean sugarWasRemoved) {
        this.sugarWasRemoved = sugarWasRemoved;
    }
}
