/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CALCULOS;

import DTO.EnviarCorreoDTO;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author juan
 */
public class EnviarCorreo {

    public void EjecutarEnvio(EnviarCorreoDTO env) {
        
        try {
            String correorecib = env.getCorreo();
            System.out.println("correo recibido " + correorecib);
            // Paso 1 estableciendo las propiedades de conexión
            java.util.Properties mailServerProperties;
            mailServerProperties = System.getProperties();
            mailServerProperties.put("mail.smtp.host", "smtp.gmail.com");
            mailServerProperties.put("mail.smtp.socketFactory.port", "465");
            mailServerProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            mailServerProperties.put("mail.smtp.auth", "true");
            mailServerProperties.put("mail.smtp.port", "465");

            javax.mail.Session getMailSession = Session.getDefaultInstance(
                    mailServerProperties,
                    new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("juan.utic.proyectos@gmail.com", "mstrpvxokhywgasq");
                }
            });

            javax.mail.internet.MimeMessage generateMailMessage;
            generateMailMessage = new MimeMessage(getMailSession);
            //Estableciendo el destino (TO)
//            generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress("juanftp100@gmail.com"));

            generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(correorecib));

            //Estableciendo el destino de la copia (CC)
//            generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress("juanftp100@gmail.com"));
            generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress(correorecib));

            //Estableciendo el destino de la copia oculta (BCC)
//            generateMailMessage.addRecipient(Message.RecipientType.BCC, new InternetAddress("juanftp100@gmail.com"));
            generateMailMessage.addRecipient(Message.RecipientType.BCC, new InternetAddress(correorecib));

            //Estableciendo el titulo del mensaje (subject)
//            generateMailMessage.setSubject("titulo del mensaje a enviar");
            generateMailMessage.setSubject("Confirmacion de reserva");

            // Estableciendo el contenido del correo electronico enriquecido(HTML)
            String bodyEmail = "<h1>Sanatorio SVETO</h1>"
                    + "<p>Estimado paciente</p>"
                    + "<p>Se acerca el dia de su reserva, favor llame en recepcion a confirmar.</p>";
            generateMailMessage.setContent(bodyEmail, "text/html");

            //Finalmente  enviamos el correo
            javax.mail.Transport.send(generateMailMessage);
            System.out.println("Se envio el email a " + correorecib);
        } catch (Exception e) {
            System.out.println("Error al enviar correo " + e);
        }
    }
//    public static void main(String[] args) {
//        
//    }
}
