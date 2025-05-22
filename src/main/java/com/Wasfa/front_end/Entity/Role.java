package com.Wasfa.front_end.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "role")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Role implements Serializable, UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_role") //
    private Integer idRole;

    @Column(nullable = false)
    private String motDePasse;

    @Column
    private String otp;

    @Column
    private LocalDateTime otpExpiration;

    @Column(nullable = false)
    private String role; // ADMIN, VETERINAIRE, GESTIONNAIRE, etc.


    private String deviceId; // pour identifier la machine

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
        return employeRanch.getEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role));
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
    @OneToOne

    @JoinColumn(name = "employe_cin", referencedColumnName = "cin")
    @JsonBackReference

    private EmployeRanch employeRanch;


}
