package com.password_manager.Controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;

// URL
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
// ResourceBundle
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import com.password_manager.App;
import com.password_manager.DB;
import com.password_manager.Models.CreditCardInfo;
import com.password_manager.Models.LoginInfo;
import com.password_manager.Models.Record;
import com.password_manager.Models.TagRel;
import com.password_manager.Models.Trash;
import com.password_manager.Models.Tag;
import com.password_manager.Models.User;

import atlantafx.base.controls.Card;
import atlantafx.base.controls.Tile;
import atlantafx.base.theme.Styles;
import atlantafx.base.theme.Tweaks;
import io.github.palexdev.materialfx.controls.MFXButton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import com.password_manager.Utils.ColorGenerator;

public class KanbanController implements Initializable {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextField searchBox;

    @FXML
    private VBox verticalBox;

    private List<Record> records = new ArrayList<>();
    private MainView mainViewController;
    private FormController formController;
    private Parent formContent;
    private String modelName;

    private static final double CARD_WIDTH = 250.0;
    private static final double CARD_HEIGHT = 150.0;
    private static final double HBOX_SPACING = 10.0;
    private static final double VERTICAL_BOX_PADDING = 10.0;

    public void initialize(URL url, ResourceBundle rb) {
        scrollPane.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldWidth, Number newWidth) {
                // System.out.println("Changed " + observableValue + " " + oldWidth + " " +
                // newWidth);
                Platform.runLater(() -> layoutCards(newWidth.doubleValue()));
            }
        });
        try {
            FXMLLoader formLoader = new FXMLLoader(App.class.getResource("fxml/form.fxml"));
            this.formContent = formLoader.load();
            this.formController = formLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void newRecord(ActionEvent event) {
        this.formController.setModelName(this.modelName);
        this.formController.setMode("new");
        this.formController.setTagsMenu();
        this.mainViewController.setFormContent(formContent);
        this.mainViewController.switchView("form");
    }

    @FXML
    void search(KeyEvent event) {
        String searchQuery = this.searchBox.getText();
        if (event.getCode() == KeyCode.ENTER && searchQuery.length() > 0) {
            this.setRecords(searchQuery);
            this.layoutCards(this.scrollPane.getWidth());
        } else if (event.getCode() == KeyCode.ENTER && searchQuery.length() == 0) {
            this.setRecords();
            this.layoutCards(this.scrollPane.getWidth());
        }
    }

    public void layoutCards(double availableWidth) {
        try {
            this.formController.setModelName(modelName);

            verticalBox.getChildren().clear();

            // Adjust availableWidth by subtracting the padding
            double adjustedWidth = availableWidth - 2 * VERTICAL_BOX_PADDING;

            // Calculate number of cards per row
            int totalCards = this.records.size();
            if (totalCards == 0) {
                return;
            }
            int cardsPerRow = (int) (adjustedWidth / (CARD_WIDTH + HBOX_SPACING));
            if (cardsPerRow <= 0) {
                cardsPerRow = 1;
            }

            // Calculate remaining space
            double totalCardWidth = cardsPerRow * CARD_WIDTH;
            double totalSpacingWidth = (cardsPerRow - 1) * HBOX_SPACING;
            double remainingSpace = adjustedWidth - totalCardWidth - totalSpacingWidth;
            double additionalWidthPerCard = remainingSpace / cardsPerRow;
            // double additionalWidthPerCard = 0;

            try {
                int cardCount = 0;

                HBox currentRow = new HBox(HBOX_SPACING);
                for (Record obj : this.records) {
                    if (cardCount >= cardsPerRow) {
                        verticalBox.getChildren().add(currentRow);
                        currentRow = new HBox(HBOX_SPACING);
                        HBox.setHgrow(currentRow, Priority.ALWAYS);
                        currentRow.setMaxHeight(Double.MAX_VALUE);
                        currentRow.setMaxWidth(Double.MAX_VALUE);
                        cardCount = 0;
                    }

                    String avatarLabel = "";
                    Color avatarColor = null;
                    String cardTitle = "";
                    TextFlow cardBody = null;
                    Boolean isFavorite = false;
                    if (obj instanceof LoginInfo) {
                        LoginInfo record = (LoginInfo) obj;
                        avatarLabel = record.getTitle().substring(0, 1).toUpperCase();
                        avatarColor = ColorGenerator.generateRandomColor(record.getTitle() + record.getCreateDate());
                        cardTitle = record.getTitle();
                        Text loginText = new Text("Login: " + record.getLogin());
                        loginText.setFill(Color.GRAY);
                        Text websiteText = new Text("\nWebsite: " + record.getWebsite());
                        websiteText.setFill(Color.GRAY);
                        cardBody = new TextFlow(loginText, websiteText);
                        cardBody.setMaxWidth(CARD_WIDTH + additionalWidthPerCard);

                        isFavorite = record.isFavorite();

                        setMouseTransparentRecursive(cardBody, true);
                    } else if (obj instanceof CreditCardInfo) {
                        CreditCardInfo record = (CreditCardInfo) obj;
                        avatarLabel = record.getTitle().substring(0, 1).toUpperCase();
                        avatarColor = ColorGenerator.generateRandomColor(record.getTitle() + record.getCreateDate());
                        cardTitle = record.getTitle();
                        Text cardHolderText = new Text("Card Holder: " + record.getCardHolderName());
                        cardHolderText.setFill(Color.GRAY);
                        Text cardNumberText = new Text("\nCard Number: " + record.getCardNumber());
                        cardNumberText.setFill(Color.GRAY);
                        cardBody = new TextFlow(cardHolderText, cardNumberText);
                        cardBody.setMaxWidth(CARD_WIDTH + additionalWidthPerCard);

                        isFavorite = record.isFavorite();

                        setMouseTransparentRecursive(cardBody, true);
                    }

                    Card card = new Card();
                    card.getStyleClass().add(Styles.ELEVATED_1);
                    card.setMinWidth(CARD_WIDTH + additionalWidthPerCard);
                    card.setMaxWidth(CARD_WIDTH + additionalWidthPerCard);

                    var avatar = new Label(avatarLabel);
                    avatar.getStyleClass().addAll(Styles.LARGE);
                    avatar.setBackground(new Background(new BackgroundFill(avatarColor, CornerRadii.EMPTY, null)));
                    avatar.setPrefSize(60, 50);
                    avatar.setAlignment(Pos.CENTER);
                    var cardHeader = new Tile(cardTitle, "", avatar);
                    MenuButton cardMenuBtn = new MenuButton(null, new FontIcon(Feather.MORE_VERTICAL));
                    if (modelName.equals("trash")) {
                        cardMenuBtn.getStyleClass().addAll(Styles.BUTTON_ICON, Tweaks.NO_ARROW);
                        MenuItem restoreBtn = new MenuItem("Restore");
                        restoreBtn.setOnAction(event -> {
                            DB.connect();
                            EntityManager entityManager = DB.entityManagerFactory.createEntityManager();
                            EntityTransaction entityTransaction = null;
                            try {
                                entityTransaction = entityManager.getTransaction();
                                entityTransaction.begin();
                                if (obj instanceof LoginInfo) {
                                    LoginInfo login = entityManager.find(LoginInfo.class, obj.getId());
                                    Trash trashRecord = entityManager
                                            .createQuery(
                                                    "FROM Trash WHERE recordModel = :model AND recordId = :recordId",
                                                    Trash.class)
                                            .setParameter("model", "login_info").setParameter("recordId", login.getId())
                                            .getSingleResult();
                                    entityManager.remove(trashRecord);
                                    entityManager.flush();
                                    entityTransaction.commit();
                                    this.setRecords();
                                    this.layoutCards(this.getWidth());
                                } else if (obj instanceof CreditCardInfo) {
                                    CreditCardInfo creditCardInfo = entityManager.find(CreditCardInfo.class,
                                            obj.getId());
                                    Trash trashRecord = entityManager
                                            .createQuery(
                                                    "FROM Trash WHERE recordModel = :model AND recordId = :recordId",
                                                    Trash.class)
                                            .setParameter("model", "credit_card_info")
                                            .setParameter("recordId", creditCardInfo.getId()).getSingleResult();
                                    entityManager.remove(trashRecord);
                                    entityManager.flush();
                                    entityTransaction.commit();
                                    this.setRecords();
                                    this.layoutCards(this.getWidth());
                                }
                                this.setRecords();
                                this.layoutCards(scrollPane.getWidth());
                            } catch (Exception e) {
                                if (entityTransaction != null) {
                                    entityTransaction.rollback();
                                }
                                e.printStackTrace();
                            } finally {
                                entityManager.close();
                            }
                            DB.disconnect();
                        });
                        cardMenuBtn.getItems().add(restoreBtn);

                        MenuItem deleteBtn = new MenuItem("Delete");
                        deleteBtn.setOnAction(event -> {
                            DB.connect();
                            EntityManager entityManager = DB.entityManagerFactory.createEntityManager();
                            EntityTransaction entityTransaction = null;
                            try {
                                entityTransaction = entityManager.getTransaction();
                                entityTransaction.begin();
                                if (obj instanceof LoginInfo) {
                                    LoginInfo login = entityManager.find(LoginInfo.class, obj.getId());
                                    Trash trashRecord = entityManager
                                            .createQuery(
                                                    "FROM Trash WHERE recordModel = :model AND recordId = :recordId",
                                                    Trash.class)
                                            .setParameter("model", "login_info").setParameter("recordId", login.getId())
                                            .getSingleResult();
                                    entityManager.remove(trashRecord);
                                    entityManager.remove(login);
                                    entityManager.flush();
                                    entityTransaction.commit();
                                    this.setRecords();
                                    this.layoutCards(this.getWidth());
                                } else if (obj instanceof CreditCardInfo) {
                                    CreditCardInfo creditCardInfo = entityManager.find(CreditCardInfo.class,
                                            obj.getId());
                                    Trash trashRecord = entityManager
                                            .createQuery(
                                                    "FROM Trash WHERE recordModel = :model AND recordId = :recordId",
                                                    Trash.class)
                                            .setParameter("model", "credit_card_info")
                                            .setParameter("recordId", creditCardInfo.getId()).getSingleResult();
                                    entityManager.remove(trashRecord);
                                    entityManager.remove(creditCardInfo);
                                    entityManager.flush();
                                    entityTransaction.commit();
                                    this.setRecords();
                                    this.layoutCards(this.getWidth());
                                }
                                this.setRecords();
                                this.layoutCards(scrollPane.getWidth());
                            } catch (Exception e) {
                                if (entityTransaction != null) {
                                    entityTransaction.rollback();
                                }
                                e.printStackTrace();
                            } finally {
                                entityManager.close();
                            }
                            DB.disconnect();
                        });
                        cardMenuBtn.getItems().add(deleteBtn);
                        cardHeader.setAction(cardMenuBtn);
                    }
                    card.setHeader(cardHeader);

                    // set TextFlow orientation
                    card.setBody(cardBody);
                    HBox footer = new HBox();
                    if (isFavorite) {
                        footer.getChildren().add(new FontIcon(Feather.STAR));
                    }
                    card.setFooter(footer);

                    setMouseTransparentRecursive(cardHeader, false, cardMenuBtn);
                    setMouseTransparentRecursive(cardBody, false, cardMenuBtn);

                    card.onMouseClickedProperty().set((event) -> {
                        this.formController.setMode("update");
                        formController.setCurrentRecord(obj);
                        mainViewController.setFormContent(formContent);
                        mainViewController.switchView("form");
                    });

                    currentRow.getChildren().add(card);
                    cardCount++;
                }

                // Add the last row if it contains any cards
                if (!currentRow.getChildren().isEmpty()) {
                    verticalBox.getChildren().add(currentRow);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setMouseTransparentRecursive(Node node, boolean value, Node... excludeNodes) {
        for (Node excludeNode : excludeNodes) {
            if (node.equals(excludeNode)) {
                return;
            }
        }
        node.setMouseTransparent(value);
        if (node instanceof Parent) {
            for (Node child : ((Parent) node).getChildrenUnmodifiable()) {
                setMouseTransparentRecursive(child, value, excludeNodes);
            }
        }
    }

    public void setRecords() {
        setRecords("");
    }

    public void setRecords(String searchQuery) {
        this.records.clear();
        DB.connect();
        EntityManager entityManager = DB.entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = null;
        try {
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            // Get User and get Userid from App Class static attribute currentUser
            User currentUser = entityManager.getReference(User.class, App.currentUser);
            // User has One2many Logins field get logins from there
            Hibernate.initialize(currentUser.getLogins());
            List<Trash> trashRecords = entityManager.createQuery("FROM Trash").getResultList();
            List<Integer> trashRecordsIds = trashRecords.stream().map(trashRecord -> trashRecord.getRecordId())
                    .toList();
            switch (modelName) {
                case "login_info":
                    List<Record> loginRecords = currentUser.getLogins().stream()
                            .filter(record -> record instanceof LoginInfo)
                            .map(record -> (Record) record)
                            .collect(Collectors.toList());
                    // Subtract trashed records
                    loginRecords = loginRecords.stream().filter(record -> !trashRecordsIds.contains(record.getId()))
                            .collect(Collectors.toList());
                    if (searchQuery.length() > 0) {
                        loginRecords = loginRecords.stream()
                                .filter(record -> ((LoginInfo) record).getTitle().toLowerCase()
                                        .contains(searchQuery.toLowerCase()))
                                .collect(Collectors.toList());
                    }
                    for (Record record : loginRecords) {
                        LoginInfo login = (LoginInfo) record;
                        List<TagRel> tagRels = entityManager
                                .createQuery("FROM TagRel WHERE recordModel = :model AND recordId = :recordId")
                                .setParameter("model", "login_info").setParameter("recordId", login.getId())
                                .getResultList();
                        List<Tag> tags = tagRels.stream().map(TagRel::getTag).toList();
                        login.setTags(tags);
                        this.records.add(login);
                    }
                    break;
                case "credit_card_info":
                    List<Record> creditCardRecords = currentUser.getCreditCardInfos().stream()
                            .filter(record -> record instanceof CreditCardInfo)
                            .map(record -> (Record) record)
                            .collect(Collectors.toList());
                    // Subtract trashed records
                    creditCardRecords = creditCardRecords.stream()
                            .filter(record -> !trashRecordsIds.contains(record.getId()))
                            .collect(Collectors.toList());
                    if (searchQuery.length() > 0) {
                        creditCardRecords = creditCardRecords.stream()
                                .filter(record -> ((CreditCardInfo) record).getTitle().toLowerCase()
                                        .contains(searchQuery.toLowerCase()))
                                .collect(Collectors.toList());
                    }
                    for (Record record : creditCardRecords) {
                        CreditCardInfo creditCardInfo = (CreditCardInfo) record;
                        List<TagRel> tagRels = entityManager
                                .createQuery("FROM TagRel WHERE recordModel = :model AND recordId = :recordId")
                                .setParameter("model", "credit_card_info")
                                .setParameter("recordId", creditCardInfo.getId())
                                .getResultList();
                        List<Tag> tags = tagRels.stream().map(TagRel::getTag).toList();
                        creditCardInfo.setTags(tags);
                        this.records.add(creditCardInfo);
                    }
                    break;
                case "trash":
                    // Convert Trash Records to their respective classes using model name
                    for (Trash trash : trashRecords) {
                        if (trash.getRecordModel().equals("login_info")) {
                            LoginInfo login = entityManager.find(LoginInfo.class, trash.getRecordId());
                            this.records.add(login);
                        } else if (trash.getRecordModel().equals("credit_card_info")) {
                            CreditCardInfo creditCardInfo = entityManager.find(CreditCardInfo.class,
                                    trash.getRecordId());
                            this.records.add(creditCardInfo);
                        }
                    }
            }
            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null) {
                entityTransaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        DB.disconnect();
    }

    public void setMainViewController(MainView mainViewController) {
        this.mainViewController = mainViewController;
        this.formController.setMainViewController(mainViewController);
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
        if (modelName.equals("trash")) {
            this.searchBox.setVisible(false);
        } else {
            this.searchBox.setVisible(true);
        }
    }

    public double getWidth() {
        return this.scrollPane.getWidth();
    }
}
