package com.password_manager.Controllers;

import io.github.palexdev.mfxcore.controls.Label;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

import org.hibernate.Hibernate;

import com.password_manager.App;
import com.password_manager.DB;
import com.password_manager.Models.CreditCardInfo;
import com.password_manager.Models.LoginInfo;
import com.password_manager.Models.User;

public class DashboardController implements Initializable {

    @FXML
    private Label weakPasswordCnt;

    @FXML
    private Label reusedPasswordCnt;

    @FXML
    private Label loginsCnt;

    @FXML
    private Label creditCardsCnt;

    public void initialize(URL location, ResourceBundle resources) {
        reusedPasswordCnt.setText("0");
        weakPasswordCnt.setText("0");
    }

    public void updateValues() {
        // Update the values of the labels
        DB.connect();
        EntityManager entityManager = DB.entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = null;
        try {
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            User currentUser = entityManager.getReference(User.class, App.currentUser);
            Hibernate.initialize(currentUser);
            // Get the count of Logins
            long loginsCount = LoginInfo.getLoginsCount(entityManager, currentUser);
            this.loginsCnt.setText(String.valueOf(loginsCount));
            // Get the count of CreditCards
            long creditCardsCount = CreditCardInfo.getCreditCardsCount(entityManager, currentUser);
            creditCardsCnt.setText(String.valueOf(creditCardsCount));

            // Get the count of reused passwords
            long reusedPasswordsCount = LoginInfo.getReusedPasswordsCount(entityManager, currentUser);
            reusedPasswordCnt.setText(String.valueOf(reusedPasswordsCount));
            // Get the count of weak passwords
            long weakPasswordCount = LoginInfo.getWeakPasswordsCount(entityManager, currentUser);
            weakPasswordCnt.setText(String.valueOf(weakPasswordCount));
            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null) {
                entityTransaction.rollback();
            }
            e.printStackTrace();
        }
    }

}
