package com.enviro.assessment.junior.candidate.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * An investor holding one portfolio and zero or more withdrawal notices.
 *
 * Relationships:
 *   Investor 1 --- 1 Portfolio          (one-to-one, owned by Portfolio)
 *   Investor 1 --- * WithdrawalNotice   (one-to-many)
 */
@Entity
@Table(name = "investor")
public class Investor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    /**
     * Used directly by the retirement-withdrawal business rule
     * (age > 65) in WithdrawalService.
     */
    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false, unique = true)
    private String email;

    @OneToOne(mappedBy = "investor", cascade = CascadeType.ALL, orphanRemoval = true)
    private Portfolio portfolio;

    @OneToMany(mappedBy = "investor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WithdrawalNotice> withdrawalNotices = new ArrayList<>();

    protected Investor() {
        // Required by JPA
    }

    public Investor(String firstName, String lastName, Integer age, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public List<WithdrawalNotice> getWithdrawalNotices() {
        return withdrawalNotices;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
