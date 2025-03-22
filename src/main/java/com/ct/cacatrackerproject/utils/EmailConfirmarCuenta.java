package com.ct.cacatrackerproject.utils;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class EmailConfirmarCuenta {

    public void emailConfirmadorCuenta(String toEmail, String activationCode){
        final String fromEmail = "your_email@example.com"; // Cambia esto
        final String password = "your_password"; // Usa variables de entorno o un gestor de secretos

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Código de activación");
            message.setText("Tu código de activación es: " + activationCode);

            Transport.send(message);
            System.out.println("Correo enviado exitosamente.");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    /*
    public boolean activateUser(String email, String userCode) throws SQLException {
        String query = "SELECT codigoactiva FROM USERS WHERE email = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            String storedCode = rs.getString("codigoactiva");
            if (storedCode.equals(userCode)) {
                String updateQuery = "UPDATE USERS SET activado = TRUE WHERE email = ?";
                PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
                updateStmt.setString(1, email);
                updateStmt.executeUpdate();
                return true;
            }
        }
        return false;
    }*/
}
