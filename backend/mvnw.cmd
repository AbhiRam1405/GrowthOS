@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements. See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership. The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License. You may obtain a copy of the License at
@REM
@REM https://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied. See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@REM ----------------------------------------------------------------------------
@REM Apache Maven Wrapper startup batch script, version 3.2.0
@REM
@REM Required ENV vars:
@REM JAVA_HOME - location of a JDK home dir
@REM
@REM Optional ENV vars
@REM MAVEN_BATCH_ECHO - set to 'on' to enable the echo of the batch script
@REM MAVEN_BATCH_PAUSE - set to 'on' to pause the script before exit
@REM MAVEN_OPTS - parameters passed to the Java VM when running Maven
@REM     e.g. to debug Maven itself, use
@REM set MAVEN_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000
@REM MAVEN_SKIP_RC - flag to disable loading of mavenrc files
@REM ----------------------------------------------------------------------------

@IF "%MAVEN_BATCH_ECHO%" == "on"  echo %MAVEN_BATCH_ECHO%

@REM Set %HOME% to equivalent of $HOME
@IF "%HOME%" == "" (SET "HOME=%HOMEDRIVE%%HOMEPATH%")

@REM Execute a user defined script before this one
@IF NOT "%MAVEN_SKIP_RC%" == "" GOTO skipRcPre
@REM check for pre script, once with legacy .bat ending and once with .cmd ending
@IF EXIST "%USERPROFILE%\mavenrc_pre.bat" CALL "%USERPROFILE%\mavenrc_pre.bat" %*
@IF EXIST "%USERPROFILE%\mavenrc_pre.cmd" CALL "%USERPROFILE%\mavenrc_pre.cmd" %*
:skipRcPre

@setlocal

@SET MAVEN_PROJECTBASEDIR=%MAVEN_BASEDIR%
@IF NOT "%MAVEN_PROJECTBASEDIR%"=="" GOTO endDetectBaseDir

@SET EXEC_DIR=%CD%
@SET WDIR=%EXEC_DIR%
:findBaseDir
@IF EXIST "%WDIR%"\.mvn GOTO baseDirFound
@IF "%WDIR%"=="%WDIR:~0,3%" GOTO baseDirNotFound
@SET "WDIR=%WDIR%\.."
@GOTO findBaseDir

:baseDirFound
@SET MAVEN_PROJECTBASEDIR=%WDIR%
@CD "%EXEC_DIR%"
@GOTO endDetectBaseDir

:baseDirNotFound
@SET MAVEN_PROJECTBASEDIR=%EXEC_DIR%
@CD "%EXEC_DIR%"

:endDetectBaseDir
@IF NOT "%MAVEN_PROJECTBASEDIR%"=="" GOTO endDetectBaseDir2
@SET MAVEN_PROJECTBASEDIR=%EXEC_DIR%
@CD "%EXEC_DIR%"
:endDetectBaseDir2

@SET MAVEN_WRAPPER_JAR="%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar"
@SET MAVEN_WRAPPER_PROPERTIES="%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.properties"
@SET DOWNLOAD_URL="https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"

@FOR /F "usebackq tokens=1,2 delims==" %%A IN (%MAVEN_WRAPPER_PROPERTIES%) DO (
    @IF "%%A"=="wrapperUrl" SET DOWNLOAD_URL=%%B
)

@IF EXIST %MAVEN_WRAPPER_JAR% (
    @IF "%MVNW_VERBOSE%" == "true" ECHO Found %MAVEN_WRAPPER_JAR%
) ELSE (
    @IF NOT "%MVNW_REPOURL%" == "" SET DOWNLOAD_URL="%MVNW_REPOURL%/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"
    @IF "%MVNW_VERBOSE%" == "true" ECHO Downloading from: %DOWNLOAD_URL%
    @CALL %SystemRoot%\System32\WindowsPowerShell\v1.0\powershell.exe -Command "& { (New-Object Net.WebClient).DownloadFile('%DOWNLOAD_URL%', '%MAVEN_WRAPPER_JAR%') }" 2>&1
    @IF "%MVNW_VERBOSE%" == "true" ECHO Finished downloading to: %MAVEN_WRAPPER_JAR%
)
@IF "%MVNW_VERBOSE%" == "true" ECHO Using MAVEN_PROJECTBASEDIR: "%MAVEN_PROJECTBASEDIR%"

@SET MAVEN_JAVA_EXE="%JAVA_HOME%\bin\java.exe"
@SET JVM_CONFIG_MAVEN_PROPS=
@SET MAVEN_EXTRA_OPTS=

@SET WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

@SET DOWNLOAD_FROM_MAVEN_CENTRAL=
@FOR /F "usebackq tokens=1,2 delims==" %%A IN (%MAVEN_WRAPPER_PROPERTIES%) DO (
    @IF "%%A"=="distributionUrl" SET INSTALL_DIR_SUFFIX=%%B
)

@%MAVEN_JAVA_EXE% %JVM_CONFIG_MAVEN_PROPS% %MAVEN_OPTS% %MAVEN_DEBUG_OPTS% -classpath %MAVEN_WRAPPER_JAR% "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" %WRAPPER_LAUNCHER% %MAVEN_CONFIG% %*
@IF ERRORLEVEL 1 GOTO error
@GOTO end

:error
SET ERROR_CODE=1

:end
@ENDLOCAL & SET ERROR_CODE=%ERROR_CODE%

@IF NOT "%MAVEN_BATCH_PAUSE%"=="on" GOTO end2
@ECHO Finished with return code %ERROR_CODE%
@PAUSE

:end2
@EXIT /B %ERROR_CODE%
