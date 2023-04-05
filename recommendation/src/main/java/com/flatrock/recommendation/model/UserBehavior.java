package com.flatrock.recommendation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.flatrock.common.model.BehaviorType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "user_behavior")
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserBehavior {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user")
    private String user;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "behavior_type")
    @Enumerated(EnumType.STRING)
    private BehaviorType behaviorType;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        UserBehavior behavior = (UserBehavior) o;
        return id != null && Objects.equals(id, behavior.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
