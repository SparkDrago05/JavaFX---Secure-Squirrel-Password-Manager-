package com.password_manager.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Iterator;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.mfxcore.controls.Label;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;

import org.hibernate.Hibernate;
import org.hibernate.PropertyValueException;

import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import atlantafx.base.theme.Styles;
import atlantafx.base.theme.Tweaks;
import com.password_manager.Utils.TagWidget;
import com.password_manager.Utils.Utils;
import com.password_manager.Utils.ColorGenerator;
import com.password_manager.Utils.PasswordGenerator;
import com.password_manager.App;
import com.password_manager.DB;
import com.password_manager.Models.CreditCardInfo;
import com.password_manager.Models.LoginInfo;
import com.password_manager.Models.Tag;
import com.password_manager.Models.TagRel;
import com.password_manager.Models.Trash;
import com.password_manager.Models.User;
import com.password_manager.Models.CardTypes;

public class FormController implements Initializable {
    @FXML
    private HBox readonlyHeader;

    @FXML
    private HBox editingHeader;

    @FXML
    private MFXButton cancelBtn;

    @FXML
    private MenuButton optionsMenu;

    @FXML
    private HBox tagBar;

    @FXML
    private MenuButton addTagBtn;

    @FXML
    private Label titleAvatar;

    @FXML
    private Label title;

    @FXML
    private MFXTextField titleField;

    @FXML
    private VBox loginInfoBox;

    @FXML
    private VBox creditCardInfoBox;

    @FXML
    private MFXTextField usernameField;

    @FXML
    private MFXTextField passwordField;

    @FXML
    private MFXButton randomPasswordBtn;

    @FXML
    private MFXTextField websiteField;

    @FXML
    private MFXDatePicker cardExpiryDateField;

    @FXML
    private MFXTextField cardHolderField;

    @FXML
    private MFXTextField cardNumberField;

    @FXML
    private MFXFilterComboBox<String> cardTypeField;

    @FXML
    private MFXDatePicker cardValidFromDateField;

    @FXML
    private MFXTextField cardVerificationCodeField;

    @FXML
    private TextArea notesField;

    private LoginInfo currentLoginsRecord;
    private CreditCardInfo currentCreditCardInfoRecord;

    private MainView mainViewController;
    private String modelName;
    private String mode; // update or new
    private List<Tag> currentTags = new ArrayList<Tag>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.editingHeader.setVisible(false);
        this.optionsMenu.getStyleClass().addAll(Styles.BUTTON_ICON, Tweaks.NO_ARROW);
        this.addTagBtn.getStyleClass().addAll(Tweaks.NO_ARROW);
        this.addTagBtn.managedProperty().bind(this.addTagBtn.visibleProperty());
        this.addTagBtn.setVisible(false);
        // this.optionsMenu.getItems().get(0).getGraphic().setStyle("-fx-icon-color:
        // red;");

        this.title.managedProperty().bind(this.title.visibleProperty());
        this.randomPasswordBtn.managedProperty().bind(this.randomPasswordBtn.visibleProperty());
        this.titleField.setVisible(false);

        // Set cardType Field options from CardType ENUM
        for (CardTypes cardType : CardTypes.values()) {
            this.cardTypeField.getItems().add(cardType.toString());
        }
    }

    public void setCurrentRecord(Object obj) {
        if (obj instanceof LoginInfo) {
            LoginInfo login = (LoginInfo) obj;
            this.currentLoginsRecord = login;
            this.currentTags = new ArrayList<>(login.getTags());
        } else if (obj instanceof CreditCardInfo) {
            CreditCardInfo creditCard = (CreditCardInfo) obj;
            this.currentCreditCardInfoRecord = creditCard;
            this.currentTags = new ArrayList<>(creditCard.getTags());
        }
        setFields();
        setTags(true);
    }

    public void setFields() {
        switch (modelName) {
            case "login_info":
                setLoginsFields();
                break;
            case "credit_card_info":
                setCreditCardFields();
                break;
            default:
                break;
        }
    }

    public void setTags(boolean readOnly) {
        MenuButton tagsMenu = (MenuButton) this.tagBar.getChildren().get(this.tagBar.getChildren().size() - 1);
        this.tagBar.getChildren().clear();
        for (Tag tag : currentTags) {
            TagWidget tagw = new TagWidget(tag.getName(), tag.getColor());
            tagw.setId(String.valueOf(tag.getId()));
            tagw.setReadonly(readOnly);
            this.tagBar.getChildren().add(tagw);
        }

        this.tagBar.getChildren().add(tagsMenu);
    }

    public void setLoginsFields() {
        this.title.setText(currentLoginsRecord.getTitle());
        this.titleField.setText(currentLoginsRecord.getTitle());
        this.titleAvatar.setText(
                (currentLoginsRecord.getTitle().length() != 0) ? currentLoginsRecord.getTitle().substring(0, 1) : "");
        this.usernameField.setText(currentLoginsRecord.getLogin());
        this.passwordField.setText(currentLoginsRecord.getPassword());
        this.websiteField.setText(currentLoginsRecord.getWebsite());

        this.titleAvatar.setBackground(
                new Background(new BackgroundFill(
                        ColorGenerator.generateRandomColor(
                                currentLoginsRecord.getTitle() + currentLoginsRecord.getCreateDate()),
                        CornerRadii.EMPTY, null)));
    }

    public void setCreditCardFields() {
        this.title.setText(currentCreditCardInfoRecord.getTitle());
        this.titleField.setText(currentCreditCardInfoRecord.getTitle());
        this.titleAvatar.setText(
                (currentCreditCardInfoRecord.getTitle().length() != 0)
                        ? currentCreditCardInfoRecord.getTitle().substring(0, 1)
                        : "");
        this.cardHolderField.setText(currentCreditCardInfoRecord.getCardHolderName());
        this.cardNumberField.setText(currentCreditCardInfoRecord.getCardNumber());
        this.cardTypeField.setValue(currentCreditCardInfoRecord.getCardType().toString());
        this.cardVerificationCodeField.setText(currentCreditCardInfoRecord.getVerificationNumber());
        this.cardExpiryDateField.setValue(currentCreditCardInfoRecord.getExpiryDate());
        this.cardValidFromDateField.setValue(currentCreditCardInfoRecord.getValidFrom());

        this.titleAvatar.setBackground(
                new Background(new BackgroundFill(
                        ColorGenerator.generateRandomColor(
                                currentCreditCardInfoRecord.getTitle() + currentCreditCardInfoRecord.getCreateDate()),
                        CornerRadii.EMPTY, null)));
    }

    @FXML
    void editForm(ActionEvent event) {
        setTagsMenu();
        toggleForm();
    }

    @FXML
    void cancelForm(ActionEvent event) {
        switch (mode) {
            case "update":
                setFields();
                setTags(false);
                toggleForm();
                break;
            case "new":
                toggleForm();
                mainViewController.switchView("kanban");
                break;
        }
    }

    @FXML
    void saveForm(ActionEvent event) {
        switch (modelName) {
            case "login_info":
                if (title.getText().isEmpty() || usernameField.getText().isEmpty() || passwordField.getText().isEmpty()
                        || websiteField.getText().isEmpty()) {
                    Utils.showErrorAlert("Error", "Please fill in all the required fields", App.getStage());
                    return;
                }
                break;
            case "credit_card_info":
                if (title.getText().isEmpty() || cardHolderField.getText().isEmpty()
                        || cardNumberField.getText().isEmpty()
                        || cardTypeField.getValue().isEmpty() || cardVerificationCodeField.getText().isEmpty()
                        || cardExpiryDateField.getValue() == null || cardValidFromDateField.getValue() == null) {
                    Utils.showErrorAlert("Error", "Please fill in all the required fields", App.getStage());
                    return;
                }
                break;
        }

        DB.connect();
        EntityManager entityManager = DB.entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            entityTransaction.begin();
            List<Tag> newTags = new ArrayList<>();
            switch (mode) {
                case "new":
                    handleNewMode(entityManager);
                    break;
                case "update":
                    handleUpdateMode(entityManager);
                    break;
            }
            entityTransaction.commit();
        } catch (PropertyValueException e) {
            if (entityTransaction != null) {
                entityTransaction.rollback();
            }
            e.printStackTrace();
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

    private void handleNewMode(EntityManager entityManager) {
        List<Tag> newTags = new ArrayList<>();
        User currentUser = entityManager.getReference(User.class, App.currentUser);
        Hibernate.initialize(currentUser);
        switch (modelName) {
            case "login_info":
                LoginInfo newLogin = createLoginInfo(currentUser);
                entityManager.persist(newLogin);
                currentLoginsRecord = newLogin;
                addTagsToEntity(newTags, newLogin.getId(), entityManager);
                break;
            case "credit_card_info":
                CreditCardInfo newCreditCard = createCreditCardInfo(currentUser);
                entityManager.persist(newCreditCard);
                currentCreditCardInfoRecord = newCreditCard;
                addTagsToEntity(newTags, newCreditCard.getId(), entityManager);
                break;
        }

        updateTags(newTags);
    }

    private void handleUpdateMode(EntityManager entityManager) {
        List<Tag> newTags = new ArrayList<>();
        List<Tag> existingTags;

        switch (modelName) {
            case "login_info":
                existingTags = updateLoginInfo(entityManager);
                break;
            case "credit_card_info":
                existingTags = updateCreditCardInfo(entityManager);
                break;
            default:
                existingTags = new ArrayList<>();
        }

        Set<Integer> existingTagIds = existingTags.stream().map(Tag::getId).collect(Collectors.toSet());
        int id = modelName.equals("login_info") ? currentLoginsRecord.getId() : currentCreditCardInfoRecord.getId();
        manageTags(entityManager, newTags, existingTagIds, id);
        updateTags(newTags);
    }

    private LoginInfo createLoginInfo(User user) {
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setTitle(this.titleField.getText());
        loginInfo.setWebsite(this.websiteField.getText());
        loginInfo.setLogin(this.usernameField.getText());
        loginInfo.setPassword(this.passwordField.getText());
        loginInfo.setNotes(this.notesField.getText());
        loginInfo.setUser(user);
        return loginInfo;
    }

    private CreditCardInfo createCreditCardInfo(User user) {
        CreditCardInfo creditCardInfo = new CreditCardInfo();
        creditCardInfo.setTitle(this.titleField.getText());
        creditCardInfo.setCardHolderName(this.cardHolderField.getText());
        creditCardInfo.setCardNumber(this.cardNumberField.getText());
        creditCardInfo.setCardType(CardTypes.valueOf(this.cardTypeField.getValue()));
        creditCardInfo.setVerificationNumber(this.cardVerificationCodeField.getText());
        creditCardInfo.setExpiryDate(this.cardExpiryDateField.getValue());
        creditCardInfo.setValidFrom(this.cardValidFromDateField.getValue());
        creditCardInfo.setNotes(this.notesField.getText());
        creditCardInfo.setUser(user);
        return creditCardInfo;
    }

    private List<Tag> updateLoginInfo(EntityManager entityManager) {
        currentLoginsRecord.setTitle(this.titleField.getText());
        currentLoginsRecord.setWebsite(this.websiteField.getText());
        currentLoginsRecord.setLogin(this.usernameField.getText());
        currentLoginsRecord.setPassword(this.passwordField.getText());
        currentLoginsRecord.setNotes(this.notesField.getText());
        entityManager.merge(currentLoginsRecord);
        return new ArrayList<>(currentLoginsRecord.getTags());
    }

    private List<Tag> updateCreditCardInfo(EntityManager entityManager) {
        currentCreditCardInfoRecord.setTitle(this.titleField.getText());
        currentCreditCardInfoRecord.setCardHolderName(this.cardHolderField.getText());
        currentCreditCardInfoRecord.setCardNumber(this.cardNumberField.getText());
        currentCreditCardInfoRecord.setCardType(CardTypes.valueOf(this.cardTypeField.getValue()));
        currentCreditCardInfoRecord.setVerificationNumber(this.cardVerificationCodeField.getText());
        currentCreditCardInfoRecord.setExpiryDate(this.cardExpiryDateField.getValue());
        currentCreditCardInfoRecord.setValidFrom(this.cardValidFromDateField.getValue());
        currentCreditCardInfoRecord.setNotes(this.notesField.getText());
        entityManager.merge(currentCreditCardInfoRecord);
        return new ArrayList<>(currentCreditCardInfoRecord.getTags());
    }

    private void addTagsToEntity(List<Tag> newTags, int id, EntityManager entityManager) {
        for (Node node : this.tagBar.getChildren()) {
            if (node instanceof TagWidget) {
                int tagId = Integer.parseInt(node.getId());
                Tag tag = entityManager.getReference(Tag.class, tagId);
                TagRel tagRel = new TagRel();
                tagRel.setTag(tag);
                tagRel.setRecordModel(this.modelName);
                tagRel.setRecordId(id);
                entityManager.persist(tagRel);
                newTags.add(tag);
            }
        }
    }

    private void manageTags(EntityManager entityManager, List<Tag> newTags, Set<Integer> existingTagIds, int id) {
        List<TagRel> tagRelsToRemove = new ArrayList<>();

        for (Node node : this.tagBar.getChildren()) {
            if (node instanceof TagWidget) {
                int tagId = Integer.parseInt(node.getId());
                Tag tag = entityManager.getReference(Tag.class, tagId);

                if (!existingTagIds.contains(tagId)) {
                    TagRel tagRel = new TagRel();
                    tagRel.setTag(tag);
                    tagRel.setRecordModel(this.modelName);
                    tagRel.setRecordId(id);
                    entityManager.persist(tagRel);
                } else {
                    existingTagIds.remove(tagId);
                }
                newTags.add(tag);
            }
        }

        if (!existingTagIds.isEmpty()) {
            List<TagRel> tagRels = entityManager.createQuery(
                    "FROM TagRel WHERE tag.id IN :tagIds AND recordModel = :model AND recordId = :recordId",
                    TagRel.class)
                    .setParameter("tagIds", existingTagIds)
                    .setParameter("model", this.modelName)
                    .setParameter("recordId", id)
                    .getResultList();
            tagRelsToRemove.addAll(tagRels);
        }

        for (TagRel tagRel : tagRelsToRemove) {
            entityManager.remove(tagRel);
        }
    }

    private void updateTags(List<Tag> newTags) {
        switch (this.modelName) {
            case "login_info":
                currentLoginsRecord.setTags(newTags);
                this.currentTags = new ArrayList<>(currentLoginsRecord.getTags());
                break;
            case "credit_card_info":
                currentCreditCardInfoRecord.setTags(newTags);
                this.currentTags = new ArrayList<>(currentCreditCardInfoRecord.getTags());
                break;
        }
        setFields();
        setTags(false);
        toggleForm();
    }

    @FXML
    void toggleFavorite(ActionEvent event) {
        DB.connect();
        EntityManager entityManager = DB.entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = null;
        try {
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            switch (modelName) {
                case "login_info":
                    currentLoginsRecord.setFavorite(!currentLoginsRecord.isFavorite());
                    entityManager.merge(currentLoginsRecord);
                    break;
                case "credit_card_info":
                    currentCreditCardInfoRecord.setFavorite(!currentCreditCardInfoRecord.isFavorite());
                    entityManager.merge(currentCreditCardInfoRecord);
                    break;
            }
            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null && !entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            e.printStackTrace(); // Print the exception for debugging
        } finally {
            entityManager.close();
        }
        DB.disconnect();
    }

    @FXML
    void duplicate(ActionEvent event) {
        DB.connect();
        EntityManager entityManager = DB.entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            switch (modelName) {
                case "login_info":
                    duplicateLoginInfo(entityManager);
                    break;
                case "credit_card_info":
                    duplicateCreditCardInfo(entityManager);
                    break;
            }
            updateTags(new ArrayList<>());
            entityTransaction.commit();
        } catch (Exception e) {
            if (!entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
        }
    }

    private void duplicateLoginInfo(EntityManager entityManager) {
        if (currentLoginsRecord != null) {
            LoginInfo duplicateLogin = new LoginInfo();
            duplicateLogin.setTitle(currentLoginsRecord.getTitle() + " (Duplicate)");
            duplicateLogin.setWebsite(currentLoginsRecord.getWebsite());
            duplicateLogin.setLogin(currentLoginsRecord.getLogin());
            duplicateLogin.setPassword(currentLoginsRecord.getPassword());
            duplicateLogin.setNotes(currentLoginsRecord.getNotes());
            duplicateLogin.setUser(currentLoginsRecord.getUser());

            entityManager.persist(duplicateLogin);
            currentLoginsRecord = duplicateLogin; // Update the current record reference if needed
        }
    }

    private void duplicateCreditCardInfo(EntityManager entityManager) {
        if (currentCreditCardInfoRecord != null) {
            CreditCardInfo duplicateCreditCard = new CreditCardInfo();
            duplicateCreditCard.setTitle(currentCreditCardInfoRecord.getTitle() + " (Duplicate)");
            duplicateCreditCard.setCardHolderName(currentCreditCardInfoRecord.getCardHolderName());
            duplicateCreditCard.setCardNumber(currentCreditCardInfoRecord.getCardNumber());
            duplicateCreditCard.setCardType(currentCreditCardInfoRecord.getCardType());
            duplicateCreditCard.setVerificationNumber(currentCreditCardInfoRecord.getVerificationNumber());
            duplicateCreditCard.setExpiryDate(currentCreditCardInfoRecord.getExpiryDate());
            duplicateCreditCard.setValidFrom(currentCreditCardInfoRecord.getValidFrom());
            duplicateCreditCard.setNotes(currentCreditCardInfoRecord.getNotes());
            duplicateCreditCard.setUser(currentCreditCardInfoRecord.getUser());

            entityManager.persist(duplicateCreditCard);
            currentCreditCardInfoRecord = duplicateCreditCard; // Update the current record reference if needed
        }
    }

    @FXML
    void delete(ActionEvent event) {
        DB.connect();
        EntityManager entityManager = DB.entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = null;
        try {
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            Trash trash = new Trash();
            trash.setRecordModel(this.modelName);
            switch (modelName) {
                case "login_info":
                    trash.setRecordId(currentLoginsRecord.getId());
                    // entityManager.remove(currentLoginsRecord);
                    break;
                case "credit_card_info":
                    trash.setRecordId(currentCreditCardInfoRecord.getId());
                    // entityManager.remove(currentCreditCardInfoRecord);
                    break;
            }
            entityManager.persist(trash);
            entityTransaction.commit();
            resetForm();
            mainViewController.switchView("kanban");
        } catch (Exception e) {
            if (entityTransaction != null && entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        DB.disconnect();

    }

    @FXML
    void createNewTag(ActionEvent event) {
        MFXGenericDialog dialogContent = MFXGenericDialogBuilder.build()
                .setContentText("djalskdjksajdlkasjdklasjdklsaj")
                .makeScrollable(true)
                .get();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("fxml/new_tag_dialog.fxml"));
        try {
            VBox root = loader.load();
            root.getStylesheets().add(App.class.getResource("css/new_tag_dialog.css").toExternalForm());
            TagDialogController tagDialogController = loader.getController();
            tagDialogController.setFormController(this);
            dialogContent.setContent(root);

            MFXStageDialog dialog = MFXGenericDialogBuilder.build(dialogContent)
                    .toStageDialogBuilder()
                    .initModality(Modality.APPLICATION_MODAL)
                    .initOwner(App.getStage())
                    .setOwnerNode(loginInfoBox)
                    .setDraggable(true)
                    .setTitle("Dialogs Preview")
                    .setScrimPriority(ScrimPriority.WINDOW)
                    .setScrimOwner(true)
                    .get();
            dialogContent.addActions(
                    Map.entry(new MFXButton("Confirm"), e -> {
                        if (tagDialogController != null) {
                            createNewTagRecord(tagDialogController);
                        }
                        dialog.close();
                    }),
                    Map.entry(new MFXButton("Cancel"), e -> dialog.close()));

            dialogContent.setMaxSize(400, 200);
            MFXFontIcon infoIcon = new MFXFontIcon("fas-circle-info", 18);
            dialogContent.setHeaderIcon(infoIcon);
            dialogContent.setHeaderText("New Tag");
            dialogContent.getStyleClass().add("mfx-info-dialog");
            dialogContent.setPadding(new Insets(5, 5, 10, 10));
            dialog.showDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createNewTagRecord(TagDialogController controller) {
        DB.connect();
        EntityManager entityManager = DB.entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            entityTransaction.begin();
            Tag newTag = new Tag();
            newTag.setName(controller.getTagName());
            newTag.setColor(controller.getTagColor());
            User currentUser = entityManager.getReference(User.class, App.currentUser);
            Hibernate.initialize(currentUser);
            newTag.setUser(currentUser);
            entityManager.persist(newTag);
            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction != null && entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        DB.disconnect();

        getTags();
        setTagsMenu();
    }

    @FXML
    void textFieldMouseEntered(MouseEvent event) {
        // It is called when the mouse enters the field.
        // If the field is not editable, then it disables the context menu.
        MFXTextField field = (MFXTextField) event.getSource();
        if (!field.isEditable()) {
            // Disable context menu
            field.setContextMenuDisabled(true);
        } else {
            // Enable context menu
            field.setContextMenuDisabled(false);
        }

    }

    @FXML
    void textAreaMouseClicked(MouseEvent event) {
    }

    @FXML
    void generateRandomPassword(ActionEvent event) {
        // Generate a random password
        String password = PasswordGenerator.generateStrongPassword(14);
        this.passwordField.setText(password);
    }

    public void setModelName(String name) {
        this.modelName = name;
        switch (modelName) {
            case "login_info":
                this.creditCardInfoBox.setVisible(false);
                this.loginInfoBox.setVisible(true);
                this.usernameField.setEditable(false);
                this.passwordField.setEditable(false);
                this.randomPasswordBtn.setVisible(false);
                this.websiteField.setEditable(false);
                this.notesField.setEditable(false);
                break;
            case "credit_card_info":
                this.loginInfoBox.setVisible(false);
                this.creditCardInfoBox.setVisible(true);
                this.cardHolderField.setEditable(false);
                this.cardNumberField.setEditable(false);
                this.cardTypeField.setEditable(false);
                this.cardVerificationCodeField.setEditable(false);
                this.cardExpiryDateField.setEditable(false);
                this.cardValidFromDateField.setEditable(false);
                break;

        }
    }

    public void setTagsMenu() {
        MenuItem firstMenuItem = this.addTagBtn.getItems().get(0);
        this.addTagBtn.getItems().clear();
        this.addTagBtn.getItems().add(firstMenuItem);

        for (Tag tag : getTags()) {
            MenuItem item = new MenuItem(tag.getName());
            item.setOnAction(e -> {
                TagWidget tagw = new TagWidget(tag.getName(), tag.getColor());
                tagw.setId(String.valueOf(tag.getId()));
                // tagw.setReadonly(false);
                this.tagBar.getChildren().add(tagw);
                this.addTagBtn.toFront();
            });
            this.addTagBtn.getItems().add(item);
        }
    }

    void toggleForm() {
        this.readonlyHeader.setVisible(!this.readonlyHeader.isVisible());
        this.editingHeader.setVisible(!this.editingHeader.isVisible());
        this.addTagBtn.setVisible(!this.addTagBtn.isVisible());
        int tags = this.tagBar.getChildren().size() - 1;
        for (int i = 0; i < tags; i++) {
            TagWidget tag = (TagWidget) this.tagBar.getChildren().get(i);
            tag.setReadonly(!tag.isReadonly());
        }
        this.title.setVisible(!this.title.isVisible());
        this.titleField.setVisible(!this.titleField.isVisible());
        this.notesField.setEditable(!this.notesField.isEditable());
        switch (modelName) {
            case "login_info":
                if (this.currentLoginsRecord != null) {
                    this.title.setText(currentLoginsRecord.getTitle());
                    this.titleAvatar.setText(currentLoginsRecord.getTitle().substring(0, 1));
                }
                this.usernameField.setEditable(!this.usernameField.isEditable());
                this.passwordField.setEditable(!this.passwordField.isEditable());
                this.randomPasswordBtn.setVisible(!this.randomPasswordBtn.isVisible());
                this.websiteField.setEditable(!this.websiteField.isEditable());
                break;
            case "credit_card_info":
                if (this.currentCreditCardInfoRecord != null) {
                    this.title.setText(currentCreditCardInfoRecord.getTitle());
                    this.titleAvatar.setText(currentCreditCardInfoRecord.getTitle().substring(0, 1));
                }
                this.cardHolderField.setEditable(!this.cardHolderField.isEditable());
                this.cardNumberField.setEditable(!this.cardNumberField.isEditable());
                this.cardTypeField.setEditable(!this.cardTypeField.isEditable());
                this.cardVerificationCodeField.setEditable(!this.cardVerificationCodeField.isEditable());
                this.cardExpiryDateField.setEditable(!this.cardExpiryDateField.isEditable());
                this.cardValidFromDateField.setEditable(!this.cardValidFromDateField.isEditable());
                break;
        }
    }

    private List<Tag> getTags() {
        DB.connect();
        EntityManager entityManager = DB.entityManagerFactory.createEntityManager();
        EntityTransaction entityTransaction = null;
        List<Tag> tags = new ArrayList<Tag>();
        try {
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
            User currentUser = entityManager.getReference(User.class, App.currentUser);
            Hibernate.initialize(currentUser.getTags());
            List<Integer> currentTagIds = this.currentTags.stream().map(tag -> tag.getId()).toList();
            tags = currentUser.getTags().stream().filter(tag -> !currentTagIds.contains(tag.getId())).toList();
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
        return tags;
    }

    public void setMode(String mode) {
        this.mode = mode;
        switch (mode) {
            case "new":
                resetForm();
                if (this.readonlyHeader.isVisible()) {
                    toggleForm();
                }
                break;
            case "update":
                if (!this.readonlyHeader.isVisible()) {
                    toggleForm();
                }
                break;
        }
    }

    public void resetForm() {
        this.titleField.setText("");
        this.titleAvatar.setText("");
        this.titleAvatar.setBackground(null);
        this.currentTags.clear();
        this.notesField.setText("");
        this.setTags(false);
        this.setTagsMenu();

        switch (modelName) {
            case "login_info":
                this.currentLoginsRecord = null;
                this.usernameField.setText("");
                this.passwordField.setText("");
                this.websiteField.setText("");
                break;
            case "credit_card_info":
                this.currentCreditCardInfoRecord = null;
                this.cardHolderField.setText("");
                this.cardNumberField.setText("");
                this.cardTypeField.setValue("");
                this.cardVerificationCodeField.setText("");
                this.cardExpiryDateField.setValue(null);
                this.cardValidFromDateField.setValue(null);
                break;
        }
    }

    MFXButton getCancelBtn() {
        return this.cancelBtn;
    }

    public void setMainViewController(MainView mainViewController) {
        this.mainViewController = mainViewController;
    }
}
