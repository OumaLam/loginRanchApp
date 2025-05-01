package com.Wasfa.front_end.services;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class addJwtToCookie {

    public static void addJwtToCookie(HttpServletResponse response,String name, String value ) {
        // Crée un cookie avec le JWT
        Cookie cookie = new Cookie( name,value);

        // Sécurise le cookie
        cookie.setHttpOnly(true);  // Pour empêcher l'accès via JavaScript
        cookie.setSecure(false);  // Pour s'assurer qu'il est transmis via HTTPS
        cookie.setPath("/");  // Le cookie est valable pour toute l'application
        cookie.setMaxAge(60 * 60 * 24);  // 1 jour (ajuster selon besoin)

        // Ajoute le cookie à la réponse
        response.addCookie(cookie);
    }
}
