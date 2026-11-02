/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ObjetoUnico;

import DTO.UsuarioDTO;

import java.util.ArrayList;

/**
 *
 * @author DELL
 */
public class ListaUsuariosAbloquear {
    private static ListaUsuariosAbloquear instancia;
    private final ArrayList<UsuarioDTO> usuariosFallidos;
    
    private ListaUsuariosAbloquear() {
        System.out.println("usuariosFallidos OK");
        usuariosFallidos=new ArrayList<>();
    }

    public static ListaUsuariosAbloquear getInstancia(){
        if (instancia == null) {
            System.out.println("Instancia OK");
            instancia=new ListaUsuariosAbloquear();
        }
        return instancia;
    } 
    
    public ArrayList<UsuarioDTO> getUsuariosFallidos(){
        return usuariosFallidos;
    }

    public void setUsuarioFallido(String usuario){
       usuariosFallidos.add(new UsuarioDTO(usuario));
    }
    
}
