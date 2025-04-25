package com.example.h2_restful.entity.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "one_time_token")
@NoArgsConstructor
@Getter
@Setter
public class OneTimeToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "expires_at", nullable = false)
    private Date expiresAt;

    @Column(name = "token", nullable = false)
    private String token;

    public OneTimeToken(User user, Date expiresAt, String token) {
        this.user = user;
        this.expiresAt = expiresAt;
        this.token = token;
    }
}
