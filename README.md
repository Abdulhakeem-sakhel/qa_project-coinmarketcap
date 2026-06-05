# CoinMarketCap UI Test Automation

Automated UI tests for [coinmarketcap.com](https://coinmarketcap.com) built with **Selenium WebDriver** and **TestNG**, following the **Page Object Model (POM)** pattern with **data-driven testing (DDT)** from CSV files.

## Tech Stack

| Tool | Version | Purpose |
|------|---------|---------|
| Java | 21 | Language |
| Maven | 3.x | Build & dependency management |
| Selenium Java | 4.39.0 | Browser automation |
| TestNG | 7.10.2 | Test framework & data providers |
| WebDriverManager | 6.1.1 | Automatic driver resolution |
| Google Chrome | latest | Target browser |

## Project Structure

```
coinmarketcap/
├── pom.xml
├── testng.xml                       # TestNG suite that runs all test classes
├── src/
│   ├── main/java/abd/coinmarketcap/
│   │   ├── AllCoinPage.java         # All-coins table (sort, filter, pagination, rows-per-page)
│   │   ├── CoinDetailPage.java      # Coin detail page (name, symbol, URL, live price)
│   │   ├── ConverterPage.java       # Crypto converter page
│   │   ├── SearchComponent.java     # Global search component
│   │   └── util/
│   │       └── RandomStringUtil.java # Random string generator for edge cases
│   └── test/
│       ├── java/abd/coinmarketcap/
│       │   ├── BaseTest.java         # WebDriver setup/teardown, base URL
│       │   ├── ConverterPageTest.java
│       │   ├── SearchTest.java
│       │   └── SortAndFilterTest.java
│       └── resources/
│           ├── converter_ddt_data.csv # Converter test data
│           └── search_ddt_data.csv    # Search test data
└── doc/
    └── CoinMarketCap_Test_Plan.docx   # Test plan
    └── Bug_Report_Converter.docs   # Test plan
    └── coinmarketcap-test cases.xlsx   # Test plan
```

## Architecture

- **Page Objects** (`src/main/java`) encapsulate locators and interactions for each page/component.
- **Tests** (`src/test/java`) extend `BaseTest`, which manages the Chrome driver lifecycle, maximizes the window, clears cookies before each test, and navigates to the page under test.
- **Data-driven tests** read scenarios from CSV files via TestNG `@DataProvider`. Each row carries a test case ID, inputs, an `ExpectedBehavior` flag, and a description. The test switches on `ExpectedBehavior` to decide which assertions to run.

## Test Coverage

### Search (`SearchTest`)
Driven by [search_ddt_data.csv](src/test/resources/search_ddt_data.csv). Behaviors:
- `SELECT_FIRST_VERIFY_COIN` – select first result and verify the coin page (URL, name, symbol, live price).
- `VERIFY_RESULTS_CONTAIN` – all results contain the keyword in name or symbol.
- `NO_RESULTS` – gibberish, special characters, numbers, and very long strings return no cryptoasset results.

Includes placeholder tokens resolved at runtime: `{WHITE_SPACE}` (leading/trailing space handling) and `{LONG_STR}` (256-char random string).

### Converter (`ConverterPageTest`)
Driven by [converter_ddt_data.csv](src/test/resources/converter_ddt_data.csv). Behaviors:
- `NUMERIC_RESULT` – a numeric conversion result is displayed for valid input.
- `SWAP_UPDATES` – swapping currencies updates the result.
- `REJECT_INPUT` – invalid input (letters, special characters) is rejected and the default value remains.

### Sort & Filter (`SortAndFilterTest`)
Exercises the all-coins table at `/coins` via the `AllCoinPage` page object. The table is lazy-loaded, so `loadTheTable()` scrolls until the row count stabilizes before any assertion.

**Sorting**
- `verifyTheDefaultTableOrdering` - the default view is sorted by Market Cap descending.
- `verifyMarketCapAsc` - toggling the Market Cap header sorts ascending.
- `verifyVolumeOrderingAsc` / `verifyVolumeOrderingDes` - toggling the Volume header sorts the volume column ascending / descending.

Order checks use a small tolerance to absorb live price fluctuations between cell reads and skip the leading advertisement row when present.

**Filtering**
- `filteringRangeMarketCap` - applying a Market Cap min/max range returns only coins whose market cap falls within that range.

**Pagination & rows-per-page**
- `goToTheSecondPage` - clicking *Next* navigates to `?page=2` and shows ranks 101-200.
- `changeRows` - changing the rows-per-page selector to 200 reloads the table and renders 200 rows.

## Prerequisites

- JDK 21
- Maven 3.x
- Google Chrome installed (the driver is downloaded automatically by WebDriverManager)

## Running the Tests

Run all tests via the TestNG suite ([testng.xml](testng.xml)), which Maven Surefire is configured to use:

```bash
mvn test
```

Run a single test class:

```bash
mvn test -Dtest=SearchTest
mvn test -Dtest=ConverterPageTest
```

You can also run the suite directly from an IDE by right-clicking `testng.xml` and choosing **Run As → TestNG Suite**.


## Author

Abdulhakeem
