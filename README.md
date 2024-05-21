# Password Manager Application
## Introduction

This is a secure and user-friendly password manager application developed in Java using JavaFX. It aims to simplify online security by managing logins, passwords, and credit card information with robust encryption and functionalities for easy organization and retrieval.

## Setup Guide

### Prerequisites
- Java Development Kit (JDK) 17 or higher installed on your system.
- Apache Maven installed on your system.
 
### Installation Steps
1. **Clone the Repository:**
   ```bash
   git clone https://github.com/SparkDrago05/JavaFX-Secure-Squirrel-Password-Manager.git
   ```
2. **Navigate to the Project Directory:**
   ```bash
   cd JavaFX-Secure-Squirrel-Password-Manager
   ```
3. **Build the Project:**
   ```bash
   mvn clean install
   ```
4. **Run the Application:**
   ```bash
   mvn javafx:run
   ```
   Alternatively, you can run the generated JAR file:
   ```bash
   java -jar target/password_manager-1.0-SNAPSHOT.jar
   ```
7. **Use the Application:**
Upon running the application, you'll be presented with the login screen. If you're a new user, you can register with your username and password. If you're an existing user, you can log in with your credentials.

## Dependencies
- Hibernate Core: 6.5.0.Final
- SQLite JDBC Driver: 3.45.3.0
- JavaFX Controls: 21
- JFoenix: 9.0.10
- Ikonli: 12.3.1
- MaterialFX: 11.17.0
- AtlantaFX Base: 2.0.1
- CSSFX: 11.4.0

## Usage
- Manage login credentials for various websites.
- Store and organize credit card information securely.
- Generate strong passwords for online accounts.
- Securely store and manage personal notes.
