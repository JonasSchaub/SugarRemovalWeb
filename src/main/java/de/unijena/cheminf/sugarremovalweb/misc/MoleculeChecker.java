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

package de.unijena.cheminf.sugarremovalweb.misc;

import de.unijena.cheminf.deglycosylation.SugarRemovalUtility;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IPseudoAtom;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Performs checks and preprocessing on input molecules like connectivity, size, atom types, pseudo atoms, etc.
 *
 * @author Maria Sorokina (https://github.com/mSorok)
 * @author Jonas Schaub (https://github.com/JonasSchaub)
 */
@Service
public class MoleculeChecker {
    //<editor-fold desc="Public static final class constants">
    //TODO: change these numbers or remove them? The SRU has no such input restrictions
    /**
     * minimum heavy atom count for input molecules.
     */
    public static final int MIN_HEAVY_ATOM_COUNT = 5;
    //
    /**
     * Maximum heavy atom count for input molecules.
     */
    public static final int MAX_HEAVY_ATOM_COUNT = 210;
    //</editor-fold>
    //
    //<editor-fold desc="Private class variables">
    /**
     * Molecules connectivity checker instance for checking whether a molecule consists of one or
     * multiple disconnected parts.
     */
    private MoleculeConnectivityChecker mcc;
    //</editor-fold>
    //
    //<editor-fold desc="Public methods">
    //TODO: are all the checks and preprocessings necessary?
    /**
     * Checks and preprocesses the input molecule for deglycosylation. Selects the biggest unconnected
     * subpart of the structure if there are multiple, filters if the molecules is too small or too big,
     * homogenizes pseudo atom representation, sets atom types, and adds implicit hydrogen atoms if there
     * are incomplete valences.
     *
     * @param molecule input molecule
     * @return the preprocessed molecule or null if the input failed a check
     */
    public IAtomContainer checkMolecule(IAtomContainer molecule){
        //check connectivity and select biggest unconnected subpart if necessary
        this.mcc = BeanUtil.getBean(MoleculeConnectivityChecker.class);
        List<IAtomContainer> listAC = this.mcc.checkConnectivity(molecule);
        if (listAC.size() == 0) {
            return null;
        } else if (listAC.size() == 1) {
            molecule = listAC.get(0);
        } else {
            //listAC.size() > 1
            molecule = SugarRemovalUtility.selectBiggestUnconnectedFragment(molecule);
        }
        //determine number of heavy atoms
        int nbheavyatoms = 0;
        for(IAtom a : molecule.atoms()){
            if(!a.getSymbol().equals("H")){
                nbheavyatoms++;
            }
        }
        if(nbheavyatoms < MoleculeChecker.MIN_HEAVY_ATOM_COUNT || nbheavyatoms > MoleculeChecker.MAX_HEAVY_ATOM_COUNT){
            return null;
        }
        //Homogenize pseudo atoms - all pseudo atoms (PA) as a "*"
        for (int u = 1; u < molecule.getAtomCount(); u++) {
            if (molecule.getAtom(u) instanceof IPseudoAtom) {
                molecule.getAtom(u).setSymbol("*");
                molecule.getAtom(u).setAtomTypeName("X");
                ((IPseudoAtom) molecule.getAtom(u)).setLabel("*");
            }
        }
        //Addition of implicit hydrogens & atom types
        CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher.getInstance(molecule.getBuilder());
        for (int j = 0; j < molecule.getAtomCount(); j++) {
            IAtom atom = molecule.getAtom(j);
            IAtomType type = null;
            try {
                type = matcher.findMatchingAtomType(molecule, atom);
                AtomTypeManipulator.configure(atom, type);
            } catch (CDKException e) {
                Logger.getLogger(MoleculeChecker.class.getName()).log(Level.WARNING, e.toString(), e);
            }
        }
        CDKHydrogenAdder adder = CDKHydrogenAdder.getInstance(molecule.getBuilder() );
        try {
            adder.addImplicitHydrogens(molecule);
        } catch (CDKException e) {
            Logger.getLogger(MoleculeChecker.class.getName()).log(Level.WARNING, e.toString(), e);
        }
        try {
            AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
            AtomContainerManipulator.percieveAtomTypesAndConfigureUnsetProperties(molecule);
        } catch (CDKException e) {
            Logger.getLogger(MoleculeChecker.class.getName()).log(Level.WARNING, e.toString(), e);
        }
        return molecule;
    }
    //</editor-fold>
}
