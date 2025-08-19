# My Maven Project

## Overview
This project is a Maven-based Java application that represents an asset management system. The main class, `Ativo`, encapsulates the properties of an asset, including its ID, name, symbol, and current price.

## Project Structure
```
my-maven-project
├── src
│   ├── main
│   │   ├── java
│   │   │   └── ProjetoTioPatinhas
│   │   │       └── Ativo.java
│   │   └── resources
│   └── test
│       ├── java
│       └── resources
├── pom.xml
└── README.md
```

## Files Description

- **pom.xml**: The Maven Project Object Model file that defines the project structure, dependencies, build settings, and plugins. It includes the group ID, artifact ID, version, and compiler settings. It also specifies dependencies such as the Oracle JDBC driver and plugins for compiling and executing the main class.

- **src/main/java/ProjetoTioPatinhas/Ativo.java**: Contains the `Ativo` class, which represents an asset with properties such as `id`, `nome`, `simbolo`, and `cotacao`. It includes a constructor and getter/setter methods for each property.

- **src/main/resources**: Directory for resource files needed by the application, such as configuration files or property files.

- **src/test/java**: Directory for test classes, which will contain unit tests for the application.

- **src/test/resources**: Directory for resource files needed for testing, such as test configuration files.

## Usage
To build the project, navigate to the project root directory and run:
```
mvn clean install
```

To run the application, use:
```
mvn exec:java -Dexec.mainClass="ProjetoTioPatinhas.Ativo"
```

## License
This project is licensed under the MIT License.