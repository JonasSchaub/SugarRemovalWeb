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

package de.unijena.cheminf.sugarremovalweb.readers;

import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;

@Service
public class UserInputMoleculeReaderService {

    public boolean verifySMILES(String smiles){

        if(smiles.isEmpty()){
            return false;
        }


        SmilesParser sp  = new SmilesParser(SilentChemObjectBuilder.getInstance());
        System.out.println(smiles);
        try {
            IAtomContainer m   = sp.parseSmiles(smiles);

        } catch (InvalidSmilesException e) {
            System.out.println(e.toString());
            return false;
        }
        return true;
    }



    public String transformToSMI(String smiles){

        String localSmiFile = "upload-dir/pasted_molecule_"+ Calendar.getInstance().getTime().getTime()+".smi";

        smiles = smiles.replaceAll("\\r|\\n", "");

        smiles = smiles + " "+"UI_"+Calendar.getInstance().getTime().getTime();

        try {


            File f = new File(localSmiFile);
            f.createNewFile();

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(f)));

            bw.write(smiles);
            bw.newLine();

            bw.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

        return localSmiFile;
    }


    public String transformToMOL(String jsonMol){
        String localMolFile = "upload-dir/sketched_molecule_"+ Calendar.getInstance().getTime().getTime()+".mol";
        System.out.println(localMolFile);

        jsonMol = jsonMol.replace("\"", "");
        jsonMol = jsonMol.replace("\\n", "@");

        String [] lines = jsonMol.split("@");
        System.out.println(lines[0]);

        System.out.println(lines);

        try {
            File f = new File(localMolFile);
            //f.getParentFile().mkdirs();
            f.createNewFile();


            FileOutputStream fos = new FileOutputStream(f);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            for (int i = 0; i < lines.length; i++) {
                bw.write(lines[i]);
                bw.newLine();
            }

            bw.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

        return  localMolFile;


    }

}
