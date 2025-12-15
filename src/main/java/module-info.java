module iut.ga.resquizz {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;
    requires javafx.base;
    requires com.google.gson;

    opens iut.ga1.resquizz.vues to javafx.fxml;
    opens iut.ga1.resquizz.controleurs to javafx.fxml;
    opens iut.ga1.resquizz.util to com.google.gson;
    opens iut.ga1.resquizz.modeles to com.google.gson, javafx.fxml;

    exports iut.ga1.resquizz.modeles;
    exports iut.ga1.resquizz.util;
}