module com.password_manager {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires org.hibernate.orm.core;
    requires transitive jakarta.persistence;
    requires javafx.graphics;
    requires com.jfoenix;
    requires MaterialFX; // Include MaterialFX as an automatic module
    requires atlantafx.base;
    requires fr.brouillard.oss.cssfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.feather;

    opens com.password_manager to javafx.fxml, org.hibernate.orm.core, fontawesomefx, com.jfoenix, MaterialFX, atlantafx.base, org.kordamp.ikonli.javafx, org.kordamp.ikonli.feather;
    opens com.password_manager.Controllers to javafx.fxml, org.hibernate.orm.core, fontawesomefx, com.jfoenix, MaterialFX, atlantafx.base, org.kordamp.ikonli.javafx, org.kordamp.ikonli.feather;
    opens com.password_manager.Models to org.hibernate.orm.core;
    exports com.password_manager;
    exports com.password_manager.Controllers;
    exports com.password_manager.Models;
}
