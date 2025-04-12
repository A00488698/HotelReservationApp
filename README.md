# HotelReservationApp

**HotelReservationApp** is an Android-based hotel reservation system developed for the MCDA 5550 project. It enables users to search for hotels, view availability, input guest information, and complete a reservation with confirmation. The app integrates both modern and traditional Android UI techniques, including Jetpack Compose and XML-based Views, and follows the MVVM architectural pattern for clean separation of concerns.

---

## Project Overview

The project consists of four main UI screens and communicates with a backend API (with a MySQL database) to handle hotel listings and reservation submissions.

### **Screen 1 – Search Parameters**
- Users select **check-in** and **check-out** dates using a traditional `DatePicker` component.
- Users input the **number of guests** and **customer name**.
- Validations ensure check-in is not in the past and check-out is at least one day after check-in.
- A **search button** triggers navigation to the next screen and stores customer data using Shared Preferences.
- When the app is reopened, the **previous input values are automatically restored** for a seamless experience.

### **Screen 2 – Hotel List**
- Displays **search parameters** passed from Screen 1.
- Uses a **reusable `HotelItemComponent.kt`** to display hotel entries retrieved from an API.
- Each item shows the **hotel name**, **price**, and **availability**.
- Users can only select available hotels; attempting to click on an unavailable hotel shows a toast warning.
- The selected hotel is passed to Screen 3 using navigation arguments.

### **Screen 3 – Guest Details & Reservation**
- Displays **hotel name**, **price**, **check-in/out dates**, **guest count**, and **customer name** for review.
- Dynamically generates guest input forms using the reusable `GuestForm.kt` component.
- Each guest must input their **name** and **select gender**.
- Form submission is blocked if any fields are empty.
- On submission, data is sent to the backend via Retrofit, and a confirmation number is returned.

### **Screen 4 – Confirmation**
- Displays a **thank-you message** and a confirmation number returned by the server.
- The confirmation number mimics real-world booking formats, composed of three random letters and four digits (e.g., ABC1234).
- Includes a **back to home** button for a clean return to the start screen.

---

## System Architecture and Components

- **Programming Language:** Kotlin
- **UI Framework:** Jetpack Compose (with XML DatePicker integration)
- **Architecture:** MVVM (Model-View-ViewModel)
    - **Model:** Located in `model/` – contains `Hotel`, `Guest`, `ReservationRequest`, `ReservationResponse`
    - **View:**
        - `ui/screens/`:
            - `Screen1.kt`: Search form screen
            - `Screen2.kt`: Hotel list screen
            - `Screen3.kt`: Guest form and confirmation
            - `Screen4.kt`: Confirmation display
        - `ui/components/`:
            - `HotelItemComponent.kt`: Reusable hotel card
            - `GuestForm.kt`: Reusable guest input form
            - `AppHeaderBar.kt`: Custom top bar with optional back button
            - `TraditionalDatePickerField.kt`: XML-style date picker embedded in Compose
    - **ViewModel:** Located in `viewmodel/` – `BookingViewModel.kt` handles app state, form validation, and API calls.
- **Networking:**
    - Retrofit + OkHttp
    - HTTP Logging Interceptor for debugging
    - `api/` folder:
        - `HotelApi.kt`: Defines API endpoints
        - `RetrofitClient.kt`: Configures Retrofit instance and HTTP client
- **Navigation:**
    - Implemented using Navigation Compose
    - Defined in `navigation/NavGraph.kt` with parameterized routes and screen transitions
- **Data Storage:**
    - **Shared Preferences** for local state (e.g., name and guest count)
    - **MySQL** database on backend for hotel and reservation storage

---

## Additional Highlights (Beyond Assignment Requirements)

- **Date Restrictions:** Users cannot choose past dates. Check-out must be at least one day after check-in.
- **Form Validation:**
    - Customer name and all guest names must be entered
    - Errors are shown inline before submission
- **Hotel Availability Enforcement:** Unavailable hotels are disabled. A toast message informs the user.
- **Confirmation Number Format:** Returns a booking ID with a realistic format (e.g., ABC1234).
- **Total Cost Calculation:** Total price is shown before final submission, based on nights × guests × rate.
- **State Restoration:** Customer name and guest count persist between sessions via Shared Preferences.
- **Back Navigation:** All screens have back navigation. Screen 4 includes a return-to-home option.

---

## Usability and Usefulness

### **Usability**
- **User-friendly Input Forms:** The app uses a clean, intuitive interface for inputting reservation details, including native date pickers and clearly labeled text fields.
- **Validation and Feedback:** Inline error messages guide users when required fields are left empty, enhancing form usability.
- **Visual Feedback for Hotel Selection:** Only available hotels can be selected, and unavailable ones show toast warnings—reducing user confusion and error rates.
- **State Persistence:** User inputs like name and guest count are retained between sessions via Shared Preferences, offering a seamless experience.
- **Responsive Navigation:** All screens support back navigation, and the confirmation screen allows quick return to the home screen, improving overall flow.

### **Usefulness**
- **Core Booking Functionality:** The app allows users to select dates, choose hotels, and register guest details—covering essential hotel reservation workflows.
- **Total Price Calculation:** Users are informed of the full cost before submission, promoting transparency and trust.
- **Realistic Booking IDs:** Confirmation numbers resemble real-world formats (e.g., ABC1234), reinforcing professionalism and user confidence.
- **Mobile Accessibility:** As a native Android app, it is well-suited for mobile usage, allowing users to book anytime, anywhere.
- **Extensibility:** With a modular architecture (MVVM, reusable components, API abstraction), the app can easily be extended with new features such as user login or payment integration.

---

## Challenges Faced During Development

While developing the HotelReservationApp, several practical challenges were encountered and resolved:

- **Material3 DatePicker Limitations:**
    - Initially, the `DatePicker` provided by Material3 (M3) was used, but it lacked flexibility for setting minimum/maximum dates or customizing date constraints.
    - To overcome this, the default `DatePicker` was replaced with the traditional XML-based `DatePicker` embedded via `AndroidView` in Jetpack Compose, offering full control over date selection and restrictions.

- **Gradle Dependency Management:**
    - Managing dependencies in the `build.gradle.kts` file required careful attention, especially due to Compose version alignment using a **BOM (Bill of Materials)**.
    - Some libraries (e.g., `material3`, `datetime`, `navigation-compose`) had specific version constraints or incompatibilities that had to be resolved by adjusting versions manually or through `libs.versions.toml`.

- **UI Component Integration (Compose + XML):**
    - Mixing Jetpack Compose UI with traditional XML views (like `DatePicker`) introduced complexity in handling context, lifecycle, and theme consistency.
    - Careful wrapping with `AndroidView` and isolation of legacy UI components helped integrate them smoothly into the Compose structure.
  
---
##  Deployment Details

- The backend API is **deployed to PythonAnywhere**, a free and lightweight cloud hosting platform for Python applications.
- The app communicates with this hosted API via the following base URL: https://Sarahzyx.pythonanywhere.com/
- The backend is connected to a **MySQL database**, allowing the app to:
  - Fetch hotel data (e.g., name, availability, pricing)
  - Submit and store reservation requests
  - Generate confirmation numbers dynamically

---

## Future Improvements

- **Enhanced UI/UX:** Add animations, better visual hierarchy, and dynamic theming.
- **User Authentication:** Enable account creation and login for personalized history.
- **Error Handling:** Show user-friendly error messages for failed API requests.
- **Testing:** Add unit tests and UI automation for production-quality stability.

---
