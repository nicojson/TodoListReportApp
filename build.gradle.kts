plugins {
    java
    application
    id("org.javamodularity.moduleplugin") version "1.8.12"
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.beryx.jlink") version "2.25.0"
}

group = "tecnm.celaya.edu.mx"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val junitVersion = "5.10.2"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

application {
    mainModule.set("tecnm.celaya.edu.mx.todolistreportapp")
    mainClass.set("tecnm.celaya.edu.mx.todolistreportapp.MainApplication")
}

tasks.named<JavaExec>("run") {
    jvmArgs = listOf(
        "--add-opens", "javafx.controls/com.sun.javafx.scene.control.behavior=com.jfoenix",
        "--add-opens", "javafx.base/com.sun.javafx.binding=com.jfoenix",
        "--add-opens", "javafx.graphics/com.sun.javafx.stage=com.jfoenix",
        "--add-opens", "javafx.base/com.sun.javafx.event=com.jfoenix",
        "--add-opens", "java.base/java.lang.reflect=com.jfoenix"
    )
}

javafx {
    version = "17.0.6"
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.web", "javafx.swing")
}

dependencies {
    implementation("com.mysql:mysql-connector-j:9.4.0")
    implementation("org.controlsfx:controlsfx:11.2.1")
    implementation("com.dlsc.formsfx:formsfx-core:11.6.0") { exclude(group = "org.openjfx") }
    implementation("net.synedra:validatorfx:0.5.0") { exclude(group = "org.openjfx") }
    implementation("org.kordamp.ikonli:ikonli-javafx:12.3.1")
    implementation("org.kordamp.ikonli:ikonli-materialdesign2-pack:12.3.1")
    implementation("com.jfoenix:jfoenix:9.0.10")

    // PDF Generation
    implementation("com.itextpdf:kernel:7.2.5")
    implementation("com.itextpdf:layout:7.2.5")

    // XLSX (Excel) Generation
    implementation("org.apache.poi:poi:5.2.3")
    implementation("org.apache.poi:poi-ooxml:5.2.3")

    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jlink {
    imageZip.set(layout.buildDirectory.file("/distributions/app-${javafx.platform.classifier}.zip"))
    options.set(listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages"))
    launcher {
        name = "app"
    }
}
