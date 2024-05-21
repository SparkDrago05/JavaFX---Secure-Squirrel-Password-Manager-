package com.password_manager.Models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "credit_card_info")

public class CreditCardInfo implements Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(name = "cardholder_name", nullable = false, length = 100)
    private String cardHolderName;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_type", nullable = false, length = 20)
    private CardTypes cardType;

    @Column(name = "card_number", nullable = false, length = 100)
    private String cardNumber;

    @Column(name = "verification_number", nullable = false, length = 5)
    private String verificationNumber;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @Column(name = "valid_from", nullable = false)
    private LocalDate validFrom;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Boolean favorite = false;

    @Column(name = "create_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
    private LocalDateTime createDate;

    @Transient
    private transient List<Tag> tags = new ArrayList<>();

    @PrePersist
    protected void onEntityPersist() {
        if (createDate == null) {
            createDate = LocalDateTime.now();
        }
        if (favorite == null) {
            favorite = false;
        }
    }

    public CreditCardInfo() {
    }

    public CreditCardInfo(int id, String title, String cardHolderName, String cardNumber, CardTypes cardType,
            LocalDate expiryDate, String notes) {
        this.id = id;
        this.title = title;
        this.cardHolderName = cardHolderName;
        this.cardNumber = cardNumber;
        this.cardType = cardType;
        this.expiryDate = expiryDate;
        this.notes = notes;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public CardTypes getCardType() {
        return cardType;
    }

    public void setCardType(CardTypes cardType) {
        this.cardType = cardType;
    }

    public String getVerificationNumber() {
        return verificationNumber;
    }

    public void setVerificationNumber(String verificationNumber) {
        this.verificationNumber = verificationNumber;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean isFavorite() {
        return (this.favorite == null) ? false : this.favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    @Transient
    public List<Tag> getTags() {
        return tags;
    }

    @Transient
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public static long getCreditCardsCount(EntityManager entityManager, User user) {
        String query = "SELECT COUNT(*) FROM credit_card_info WHERE user_id = :userId";
        Number result = (Number) entityManager.createNativeQuery(query)
                .setParameter("userId", user.getId())
                .getSingleResult();
        return result.longValue();
    }

}
