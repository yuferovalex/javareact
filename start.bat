@echo off

REM --------------------------------------------------------------
REM Start database server
REM --------------------------------------------------------------

PUSHD .\shared\mariadb\bin
START "Database" mysqld --console
POPD

REM --------------------------------------------------------------
REM Start backend server
REM --------------------------------------------------------------

REM Wait 5 sec
PING -n 5 ya.ru > nul

START "Backend server" java "-Dfile.encoding=UTF-8" -jar .\target\site-0.0.1-SNAPSHOT.jar

REM --------------------------------------------------------------
REM Start frontend server
REM --------------------------------------------------------------

REM Wait 5 sec
PING -n 5 ya.ru > nul

PUSHD .\frontend
START "Frontend server" ..\shared\nodejs\npm start
POPD