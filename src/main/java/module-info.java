/**
 * This module specifies the dependencies and exported packages for the
 * com.example application.
 * It requires the transitive javafx.graphics module and the javafx.controls
 * module.
 * This module exports the com.example package and the com.example.data package
 * to make them accessible o other modules that depend on this module.
 * 
 */
module com.example {
    requires transitive javafx.graphics;
    requires javafx.controls;

    exports com.example;
    exports com.example.data;
}
