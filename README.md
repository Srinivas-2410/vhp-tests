# vhp-tests

## Overview

**vhp-tests** is an automated testing framework designed for validating the functionality, reliability, and user experience of the [VHP Karnataka website](https://vhpkarnataka.org/). Built using Java, Selenium WebDriver, and TestNG, this project offers robust end-to-end tests, including UI validation, functional testing, broken link checks, and form submission verifications. The framework also integrates with ExtentReports for detailed and visually rich test reporting.

---

## Features

- **Web UI Automation**: Tests critical website flows using Selenium WebDriver.
- **TestNG Integration**: Organizes tests, supports parallel execution, and provides flexible configuration.
- **Data-Driven Testing**: Reads test data from external CSV files (e.g., for donation form validation).
- **Advanced Reporting**: Generates ExtentReports with visual summaries, system information, and embedded screenshots for failed tests.
- **Logging**: Uses Log4j2 for detailed logging of test execution and outcomes.
- **Screenshot Capture**: Automatically captures screenshots on test failures for easier debugging.
- **Modular Design**: Test cases inherit from a shared `BaseTest` class for consistent setup and teardown.

---

## Test Suite Structure

| Module/Test             | Description                                                                 |
|-------------------------|-----------------------------------------------------------------------------|
| `GalleryTest`           | Validates image loading and visibility on the Multimedia Gallery page.      |
| `DonatePageTest`        | End-to-end testing of the donation form, using data-driven input from CSV.  |
| `BrokenLinksTest`       | Checks for dead or broken links on the homepage to ensure site health.      |
| `ExtentReportListener`  | Listens to test events and generates ExtentReports with screenshots, logs.  |

---

## Directory Layout

```
src/
 └── test/
     ├── java/
     │    ├── tests/
     │    │    ├── GalleryTest.java
     │    │    ├── DonatePageTest.java
     │    │    └── BrokenLinksTest.java
     │    └── listeners/
     │         └── ExtentReportListener.java
     └── resources/
           └── donation_data.csv
```

---

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven or Gradle
- Chrome browser (default; update code for other browsers if needed)

### Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/Srinivas-2410/vhp-tests.git
   cd vhp-tests
   ```

2. **Install dependencies**
   - Using Maven:
     ```bash
     mvn clean install
     ```

3. **Configure WebDriver**
   - Ensure the appropriate ChromeDriver is available in your system `PATH`, or configure its location in `BaseTest`.

4. **Test Data**
   - Edit `src/test/resources/donation_data.csv` to add or update data-driven scenarios for `DonatePageTest`.

### Running Tests

- **All Tests:**
  ```bash
  mvn test
  ```

- **Specific Test Class:**
  ```bash
  mvn -Dtest=GalleryTest test
  ```

### Test Reports

- After execution, detailed HTML reports are generated in:
  ```
  ./test-output/ExtentReports/
  ```
  Open the latest `.html` report in your browser to view results, logs, and screenshots.

---

## Key Technologies Used

- **Java**: Programming language for automation logic.
- **Selenium WebDriver**: For browser automation.
- **TestNG**: Testing framework for organizing and executing tests.
- **Log4j2**: Logging framework.
- **ExtentReports**: Advanced reporting with screenshots and logs.

---

## Customization & Extending

- **Adding New Tests**: Create new test classes in `src/test/java/tests/` and extend `BaseTest`.
- **Custom Reports**: Edit `ExtentReportListener` to customize report themes, metadata, or output paths.
- **Browser Support**: Update `BaseTest` to add support for different browsers or remote execution.

---

## Contributing

1. Fork the repo and create your feature branch (`git checkout -b feature/AmazingTest`)
2. Commit your changes (`git commit -am 'Add awesome test'`)
3. Push to the branch (`git push origin feature/AmazingTest`)
4. Create a Pull Request

---

## License

This project is for educational and demonstration purposes. Please refer to the repository for licensing details or open an issue for clarification.

---

## Maintainer

[Srinivas K S](https://github.com/Srinivas-2410)

---

## Acknowledgments

- Inspired by real-world QA automation best practices.
- Thanks to the developers and contributors of Selenium, TestNG, Log4j2, and ExtentReports.
