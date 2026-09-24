API Automation Assignment: Banking Test Suite Project Overview This project is an automated API testing framework for a banking application, named api-automation-assignment. It validates core business behaviors including the customer lifecycle, account management, and fund transfers.

The framework is built using Java and Maven. It utilizes a Behavior-Driven Development (BDD) approach with Cucumber, leveraging Rest-Assured for all API interactions and TestNG as the test runner.

Execute the following command to run the suite mvn clean test Viewing the Reports After the test execution finishes, TestNG and Cucumber will generate execution reports in the target directory. Navigate to the root directory of the project (where the pom.xml is located). Navigate to the target/surefire-reports/ folder.

Open index.html or emailable-report.html in your web browser to view the test results, including any failed assertions and logged diagnostics.

Technology Stack Build Tool: Maven. XML

API Client: Rest-Assured (v5.3.0).

BDD Framework: Cucumber Java (v7.11.1)

Test Runner: Cucumber TestNG (v7.11.1)

State Management: Cucumber PicoContainer (v7.11.1) for dependency injection

Data Binding: Jackson Databind (v2.15.2) for JSON serialization and deserialization

IDE Support: The project includes configuration files (.classpath and .project) configured for the Eclipse IDE, utilizing the standard JRE container.

What is Present (Project Structure) Based on standard Maven conventions and the provided configurations, the project consists of the following components:

pom.xml: The Maven Project Object Model file containing all required dependencies (Rest-Assured, Cucumber, TestNG, Jackson, PicoContainer)

src/test/java/: The primary directory containing the Java test code. This includes:
Models/POJOs: Java classes representing the JSON request and response payloads (e.g., Customer, Account, Transfer).

Step Definitions: The Java code implementing the logic for the Gherkin feature steps.

Utils/Context: Shared state containers (via PicoContainer) for passing dynamic data between steps.

src/test/resources/: The directory containing non-Java testing resources. This includes the Cucumber .feature files and any configuration properties.

.gitignore: Configured to ignore build output files, specifically excluding the /target/ directory from version control.
TXT

.project & .classpath: Eclipse-specific configuration files for defining the build commands, natures, and source/output paths.

Prerequisites Java Development Kit (JDK): Java 11 or higher installed and configured in your system PATH (Note: The Eclipse classpath is currently mapped to a JavaSE-1.8 container).
Unknown

Maven: Apache Maven installed and configured in your system PATH.

Network: An active internet connection to reach the external banking API and download dependencies.

