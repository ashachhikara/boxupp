@echo off

rem Boxupp startup script 
rem www.boxupp.com
rem Powered by Paxcel Technologies Pvt Ltd
rem Copyright 2013 Paxcel Technologies Pvt Ltd


SETLOCAL ENABLEEXTENSIONS
IF ERRORLEVEL 1 ECHO Unable to enable extensions GOTO end

rem *** Check Vagrant is installed or not

if not "x%PATH:Vagrant=%" == "x%PATH%" goto check_virtualbox
wget  http://www.boxupp.com/data/vagrant.msi
IF errorlevel 1 goto vagrantend
vagrant.msi

rem ***Check Virtual Box is installed in machine or not

:check_virtualbox
REG QUERY HKEY_LOCAL_MACHINE\SOFTWARE\Oracle\VirtualBox >nul 2>&1

IF %errorlevel%==0 GOTO up
if %errorlevel%==1 GOTO install_virtalbox



rem *** Install Virtual Box

:install_virtalbox
echo VirtualBox is not installed installing VirtualBox
wget  http://www.boxupp.com/data/VirtualBox-4.3.exe

rem IF %errorlevel%==0 GOTO up
IF errorlevel 1 goto virtualboxbend
VirtualBox-4.3.exe

rem Boxupp startup script
 
:up
echo Starting BoxUpp
cd ..
SET "BASEDIR="%cd%""

start java -Dfile.encoding=UTF-8 -cp %BASEDIR%;%BASEDIR%\lib\*;%BASEDIR%\config; com.boxupp.init.Boxupp

FOR /F "tokens=1,2 delims==" %%G IN (config\config.properties) DO (set %%G=%%H)
Setlocal EnableDelayedExpansion
set "host=http://localhost:" 
set "port=%port%" 
set "url=%host%%port%"
echo Boxupp is up at %url% !
endlocal

pause

goto end

:virtualboxbend
echo Error downloading Oracle VirtualBox Please Check Your Network
pause

:vagrantend
echo Error downloading Vagrant system Please Check Your Network
pause

:end

