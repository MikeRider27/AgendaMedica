/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AYUDA;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author juan
 */
public class AbrirPdf {

    public void abrirPdf(String docu) {
        try {
            File path = new File("/home/juan/NetBeansProjects/AgendaMedica_V00.2/src/java/AYUDA/" + docu + ".pdf");
            Desktop.getDesktop().open(path);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
//
//    public static void main(String[] args) {
//        AbrirPdf ab = new AbrirPdf();
//        ab.abrirPdf("1");
//    }
}
