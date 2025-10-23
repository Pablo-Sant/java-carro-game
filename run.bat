@echo off
setlocal

REM Define o classpath com todas as dependências
set JAVAFX_PATH=%USERPROFILE%\.m2\repository\org\openjfx
set CP=target/classes;^
%JAVAFX_PATH%/javafx-controls/11.0.2/javafx-controls-11.0.2-win.jar;^
%JAVAFX_PATH%/javafx-graphics/11.0.2/javafx-graphics-11.0.2-win.jar;^
%JAVAFX_PATH%/javafx-base/11.0.2/javafx-base-11.0.2-win.jar;^
%JAVAFX_PATH%/javafx-fxml/11.0.2/javafx-fxml-11.0.2-win.jar

REM Compila o projeto
call mvn clean compile

REM Executa a aplicação com os módulos necessários
java --module-path "%JAVAFX_PATH%/javafx-controls/11.0.2/javafx-controls-11.0.2-win.jar;%JAVAFX_PATH%/javafx-graphics/11.0.2/javafx-graphics-11.0.2-win.jar;%JAVAFX_PATH%/javafx-base/11.0.2/javafx-base-11.0.2-win.jar;%JAVAFX_PATH%/javafx-fxml/11.0.2/javafx-fxml-11.0.2-win.jar" --add-modules javafx.controls,javafx.fxml -cp %CP% com.example.App

pause