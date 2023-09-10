# Prescription-and-Allergies-Management-System

Prescription and Allergies Management System is an application that allow a physician to login and interact with patients viewing patient details, prescription history and accordingly keeping track and adding allergies and prescribing medicines for a patient which includes filtering medicines such that they do not have harmful allergic reactions for the patient.

For more information, visit the [Epic Jira Link](https://jira2.cerner.com/browse/DEVACDMY-36556) of Prescription and Allergies Management System.

## First Time Project Setup

The following instructions are for new developers to get started with the project.

## Tools 🛠

| Tool     | Version        | Link                                                         | Note                            |
| -------- | -------------- | ------------------------------------------------------------ | ------------------------------- |
| JDK      | 1.8.0          | [JAVA 8.0.1](https://www.oracle.com/in/java/technologies/javase/javase8u211-later-archive-downloads.html) | JDK 8 has Long Term Support.   |
| STS      | Latest Version | [Spring Tool Suite 4](https://spring.io/tools) |  |
| Git      | Latest Version | [Cerner Wiki Guide for Git](https://wiki.cerner.com/display/public/PMOConDoc/DevAcademy+Github+Setup) | Follow this guide for setup.    |
| Maven    | maven-3.8.2    | [Maven-3.8.2](https://archive.apache.org/dist/maven/maven-3/3.8.2/binaries/) | Download the zip folder.        |
| MySQL    | mysql-8.0.26   | [MySQL-8.0.26 ](https://dev.mysql.com/get/Downloads/MySQLInstaller/mysql-installer-community-8.0.26.0.msi) | Follow the installer to setup.  |
| Node.js  | 14.17.6        | [Node.js-14.17.6 ](https://nodejs.org/en/blog/release/v14.17.6/) | Follow the installer to setup.  |
| npm      | 6.14.15        |  | Will be installed with Node. Not compatible with npm 7.0   |

## Frontend Setup

### <u>Node.js</u>
1. Download and install Node.js 14.17.6 from the above link.
2. Navigate to the client folder and install required node packages.
```
npm install
```
3. Run the frontend application.
```
npm run start
```
4. Access the frontend application from browser.
```
localhost:3000
```
5. Test your code.
```
npm run test
```
6. Update snapshots during the test.
```
npm run test:update
```
7. Check your code coverage.
```
npm run coverage
```

## Backend Setup

### <u>Java</u>

1. Download and install Java 8 version from the above link.
2. Right Click on "***This PC***" -->Click "***Properties***" --> Click "***Advanced system settings***" --> Open "***Environment variables***".
3. In User variables, select "***Path***" and click "***Edit***". Click on "***New***" and enter `%JAVA_HOME\bin%` and click "***Ok***".
4. In System variables, click "New" and enter "JAVA_HOME" for "Variable Name" and directory to the Java folder for "Variable Value". For example: `C:\Program Files\Java\jdk-1.8.0`.
5. Verify the installation by running `java --version` in Command Prompt.

### <u>Maven</u>

Perform these steps if any other editor apart from IntelliJ is used. IntelliJ already has Maven embedded.

1. Unzip the contents and add a new "***System variable***" in the "***Environment variables***". Set the variable name as "M2_HOME" and the maven folder as the variable value. For example: `%C:\Program Files\apache-maven-3.8.2\` . 
2. Add the "***bin***" directory path to the System variable "***Path***". For example, Variable Name: Path, Variable Value:`C:\Program Files\apache-maven-3.6.3\bin.`  Click "***Ok***"
3. Verify the installation by using the command `mvn --version` in Command Prompt. 

### <u>Cloning the Project</u>

1. To clone the project from Github, open Git bash and use the command: `git clone "https://github.training.cerner.com/DevCenter/Prescription-and-Allergies-Management-System.git"`.
2. Go to the directory "Prescription-and-Allergies-Management-System" by using the command: `cd Prescription-and-Allergies-Management-System`.
3. Go to stable branch using the command: `git checkout stable`.

### <u>Steps for STS4</u>

#### 	**Opening the Project:**

1. Open STS4 and click on "***File*** -> ***Open Projects from File System*** -> ***Select cloned folder and click finish***".

#### 	Setting up JDK:

1. Open "***File***" --> Click on "***Project Structure***"--> Select "***Project***".
2. Select the Project SDK dropdown option and it should automatically detect the JDK version currently installed. Select and click "***Apply***" and "***Ok***".

#### 	Setting up Maven (If Maven is not bundled):

1. Open "***File***" --> Click on "***Settings***".
2. Search "Maven". Under Maven directory, paste the path of the downloaded Maven directory.
3. Click "***Apply***" and "***Ok***".

Note: For formatting, the Java source code is enforced with [fmt-maven-plugin](https://github.com/coveooss/fmt-maven-plugin) against the [google-java-format](https://github.com/google/google-java-format).

### <u>Steps for MySQL Setup</u>

#### MySQL Installation and Configuration for Windows:

1. Download the MySQL installer from link provided above.
2. Follow the installer to install MySQL server on local machine, take notes for your username, password and service name.
3. Add the "***bin***" directory path to the System variable "***Path***". For example, Variable Name: Path, Variable Value: `C:\Program Files\MySQL\MySQL Server 8.0\bin`. Click "***Ok***".
4. Start the MySQL service using Windows service UI or `net start MySQL_service_name` in command prompt.
5. Login to the database using the command `mysql -u username -p` and input your password.

### Problem Statement for Project

Prescription and allergies Management System consist of  REST APIs and a UI where the user will be able to login as a doctor.

***Doctor***: 
1.	Can view recently visited patients on the landing page and can search patients by name.
2.	Can view patient profile with demographic info, allergies, and patient history, diagnosis, and prescription by selecting patient search as well as patient list.
3.	Can add/modify allergies for the patient.
4.	Can prescribe medicine to the patient and if the patient is allergic to the substance or the medicine itself, the system shall allow suggesting an alternative medicine to prescribe.
5.	The system shall allow adding prescriptions for the patient.
6.	Logout: display login page.

#### Tech Stack used

Java (jdk1.8) + Spring boot, Spring data JPA + Hibernate, React.js, mySQL

