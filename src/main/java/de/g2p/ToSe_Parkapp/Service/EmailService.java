package de.g2p.ToSe_Parkapp.Service;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);
}
