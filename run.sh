#!/bin/bash

# Compila o projeto
mvn clean compile

# Executa o jogo usando o plugin do JavaFX
mvn javafx:run