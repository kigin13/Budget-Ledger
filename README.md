# Budget Ledger

Budget Ledger is an Android app built with Jetpack Compose that lets users manage their finances by tracking wallets, balances, and transactions in real time.

## Highlights

### Multi-Wallet Dashboard

View and switch between multiple wallet types (Primary, Secondary, Expense) with a swipeable card pager interface and real-time balance updates.

### Transaction Tracking

Monitor all your income and expenses with color-coded transaction lists — green for income and red for expenses — including descriptions, amounts, and timestamps.

### Add Transactions

Easily add new transactions using a simple form that includes a description, amount, and transaction type (Income/Expense). The app provides real-time validation and instant balance updates.

### Real-Time Balance Calculation

Automatically calculates your balance by combining your initial wallet balance with your full transaction history, updating instantly as new entries are added.

### Modern Architecture

Built using the MVVM pattern with Clean Architecture and Hilt dependency injection, ensuring a clean separation of concerns, enhanced testability, and easier maintenance.

### Networking

Powered by Retrofit and OkHttp with Kotlin coroutines and Flow support, providing efficient, type-safe, and maintainable API communication.

### Local Storage

Uses Room Database for reliable local data persistence of all transactions, ensuring that user data remains secure and accessible offline.

### Material Design 3

Features a polished interface built entirely with Jetpack Compose and Material Design 3 components for a smooth, modern user experience.

Overall, Budget Ledger showcases modern Android development best practices — blending Jetpack Compose's declarative UI, clean architecture principles, and the latest Kotlin features for a seamless financial management experience.

## API Integration

The app integrates with a custom financial data API with the following configuration:

- **Base URL:** `https://onecash.free.beeceptor.com/`
- **Endpoint:** `/api/userData` — Fetches user profile and wallet information

The app handles various API scenarios, including:

- Invalid URL handling
- Network error management
- Server response validation
- JSON parsing and error handling
- HTTP status code error responses

Tech Stack

Language: Kotlin (JVM 11)

UI Framework: Jetpack Compose

UI Design: Material Design 3

Dependency Injection: Hilt

Networking: Retrofit + OkHttp

Local Database: Room

Async Programming: Coroutines & Flow

State Management: StateFlow

Navigation: Navigation Compose


