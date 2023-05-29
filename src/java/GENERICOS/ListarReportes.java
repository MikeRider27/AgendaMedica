/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GENERICOS;

import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author DELL
 */
public class ListarReportes extends SalvajeDB {

    public void generarReportes(String urlJasper, Map parametros) throws JRException {
        try {
            try {
                this.conectar();
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(ListarReportes.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            JasperReport report = JasperCompileManager.compileReport(urlJasper);
            JasperPrint print = JasperFillManager.fillReport(report, parametros, this.conn);
            //Para visualizar el pdf directamente desde java
            JasperViewer.viewReport(print, false);
           if(this.conn != null){
                try {
                    this.desconectar();
                } catch (SQLException ex) {
                    Logger.getLogger(ListarReportes.class.getName()).log(Level.SEVERE, null, ex);
                }
           }
        } catch (JRException ex) {
            System.out.println("Genericos.ListarReportes.generarReportes()" + ex.getMessage());
        }
    }
}
