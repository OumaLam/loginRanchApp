package com.Wasfa.front_end.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "employes")
@NoArgsConstructor
@AllArgsConstructor
public class Employe implements Serializable, UserDetails {



    @Id
    @Column(nullable = false, unique = true)
    private String cin;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String motDePasse;

    @Column
    private String otp;

    @Column
    private LocalDateTime otpExpiration;

    @Column(nullable = false)
    private String fonction; // ADMIN, VETERINAIRE, GESTIONNAIRE, etc.


    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    private String deviceId; // pour identifier la machine

    @Column(nullable = false)
    private LocalDate dateEmbauche;

    @Column(nullable = false)
    private String typeContrat;

    private String token;

    @Column
    private Boolean enabled = true;
    @Column
    private Integer otpResendCount;
    @Column
    private LocalDateTime lastOtpSentTime;

    // Implémentation de UserDetails

    @Override
    public String getPassword() {
        return motDePasse;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(fonction));
        // ou return List.of(new SimpleGrantedAuthority("ROLE_" + fonction));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled != null ? enabled : true;
    }
}
