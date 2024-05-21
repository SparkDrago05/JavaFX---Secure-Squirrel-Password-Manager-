package com.password_manager.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.WhereJoinTable;

@Entity
@Table(name = "login_info")
public class LoginInfo implements Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, length = 255)
    private String website;

    @Column(nullable = false, length = 255)
    private String login;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(columnDefinition = "TEXT")
    private String notes;

    private Boolean favorite = false;

    @Column(name = "create_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
    private LocalDateTime createDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Transient
    private transient List<Tag> tags = new ArrayList<>();
    // @ManyToMany(fetch = FetchType.EAGER)
    // @JoinTable(name = "tag_rel", joinColumns = @JoinColumn(name = "res_id",
    // referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name =
    // "tag_id", referencedColumnName = "id"))
    // @WhereJoinTable(clause = "res_model = 'login_info'")
    // private List<Tag> tags = new ArrayList<>();

    @PrePersist
    protected void onEntityPersist() {
        if (createDate == null) {
            createDate = LocalDateTime.now();
        }
        if (favorite == null) {
            favorite = false;
        }
    }

    public LoginInfo() {
    }

    public LoginInfo(int id, String title, String website, String login, String password, String notes, User user) {
        this.id = id;
        this.title = title;
        this.website = website;
        this.login = login;
        this.password = password;
        this.notes = notes;
        this.user = user;
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

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    @Transient
    public List<Tag> getTags() {
        return tags;
    }

    @Transient
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public static long getLoginsCount(EntityManager entityManager, User user) {
        String query = "SELECT COUNT(*) FROM login_info WHERE user_id = :userId";
        Number result = (Number) entityManager.createNativeQuery(query)
                .setParameter("userId", user.getId())
                .getSingleResult();
        return result.longValue();
    }

    public static long getReusedPasswordsCount(EntityManager entityManager, User user) {
        String query = "SELECT COUNT(l1) " +
                "FROM LoginInfo l1 " +
                "WHERE l1.user = :user AND l1.password IN (" +
                "  SELECT l2.password " +
                "  FROM LoginInfo l2 " +
                "  WHERE l2.user = :user " +
                "  GROUP BY l2.password " +
                "  HAVING COUNT(l2.password) > 1)";
        return entityManager.createQuery(query, Long.class)
                .setParameter("user", user)
                .getSingleResult();
    }

    public static long getWeakPasswordsCount(EntityManager entityManager, User user) {
        String query = "SELECT COUNT(*) FROM login_info l " +
                "WHERE l.user_id = :userId " +
                "AND (LENGTH(l.password) < 8 " +
                "OR l.password NOT LIKE '%[0-9]%' " +
                "OR l.password NOT LIKE '%[a-z]%' " +
                "OR l.password NOT LIKE '%[A-Z]%' " +
                "OR l.password NOT LIKE '%[@#$%^&+=]%')";
        Number result = (Number) entityManager.createNativeQuery(query)
                .setParameter("userId", user.getId())
                .getSingleResult();
        return result.longValue();
    }

}
