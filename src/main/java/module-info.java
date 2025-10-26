module tecnm.celaya.edu.mx.todolistreportapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql;
    requires com.jfoenix;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.materialdesign2;

    // Abrir paquetes a los módulos que necesitan usar reflexión
    opens tecnm.celaya.edu.mx.todolistreportapp to javafx.fxml;
    opens tecnm.celaya.edu.mx.todolistreportapp.controller to com.jfoenix, javafx.fxml;
    opens tecnm.celaya.edu.mx.todolistreportapp.model to javafx.base;

    // Exportar paquetes para que sean públicamente utilizables
    exports tecnm.celaya.edu.mx.todolistreportapp;
    exports tecnm.celaya.edu.mx.todolistreportapp.controller;
    exports tecnm.celaya.edu.mx.todolistreportapp.model;
    exports tecnm.celaya.edu.mx.todolistreportapp.dao;
    exports tecnm.celaya.edu.mx.todolistreportapp.config;

}
