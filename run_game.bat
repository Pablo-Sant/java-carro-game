@echo off
echo Limpando o projeto...
call mvn clean

echo Compilando o projeto...
call mvn compile

echo Executando o jogo...
call mvn javafx:run

pause