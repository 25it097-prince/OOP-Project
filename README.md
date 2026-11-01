
# MiniBank - Digital Banking Management System

## Project Description
MiniBank is a Java-based banking management system developed as an OOP practical project. It provides basic banking operations with exception handling, multithreading, collections, serialization, and file handling.

## Features
- Open new bank accounts
- Deposit and withdraw money
- Transfer money between accounts
- View account statements
- List all accounts
- Batch deposit processing using multithreading
- Account data persistence using serialization
- Transaction reports
- Input validation
- Custom exception handling
- Audit logging using annotations and reflection

## Technologies Used
- Java
- Object-Oriented Programming
- Java Collections Framework
- Multithreading
- Java Reflection
- Serialization
- File Handling
- JUnit 5
- IntelliJ IDEA

## Project Structure
- model
- service
- util
- exception
- model.annotation
- test

## How to Run
1. Clone or download this repository.
2. Open the project in IntelliJ IDEA.
3. Build the project.
4. Run `service.MiniBank`.

To run the executable JAR:

```bash
java -jar Project.jar
```

## Testing
JUnit 5 tests verify:
- Deposit increases account balance.
- Withdrawal beyond the allowed limit throws an exception.
- Concurrent deposits maintain the correct balance.

## Author
Developed as a Java OOP MiniBank practical project.
