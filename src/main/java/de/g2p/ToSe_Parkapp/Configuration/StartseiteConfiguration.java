package de.g2p.ToSe_Parkapp.Configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
     * Konfiguriert die Startseitenweiterleitung
     *
     */
    @Configuration
    public class StartseiteConfiguration implements WebMvcConfigurer {

        /**
         * leitet Anwender zur Startseite wenn keine URL spezifiziert wird
         */
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/").setViewName("/home.html");
                registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
            }
        }

