module tecnm.celaya.edu.mx.todolistreportapp {
    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    // Base de datos
    requires java.sql;

    // UI & Controls
    requires com.jfoenix;
    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.materialdesign2;

    // iText PDF Generation
    requires kernel;
    requires layout;
    requires java.desktop;

    // Apache POI XLSX Generation
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    // Abrir paquetes para reflexión
    opens tecnm.celaya.edu.mx.todolistreportapp to javafx.fxml;
    opens tecnm.celaya.edu.mx.todolistreportapp.controller to com.jfoenix, javafx.fxml;
    opens tecnm.celaya.edu.mx.todolistreportapp.model to javafx.base;

    // Exportar paquetes para uso público
    exports tecnm.celaya.edu.mx.todolistreportapp;
    exports tecnm.celaya.edu.mx.todolistreportapp.controller;
    exports tecnm.celaya.edu.mx.todolistreportapp.model;
    exports tecnm.celaya.edu.mx.todolistreportapp.dao;
    exports tecnm.celaya.edu.mx.todolistreportapp.config;
    exports tecnm.celaya.edu.mx.todolistreportapp.service;
}
