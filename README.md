# SIT708 Task 2.1P

A simple Android App designed for Task 2.1P. This app supports **Currency**, **Fuel**, and **Temperature** conversions.

---

## Features

* **Currency Conversion**

  * Supports USD, AUD, EUR, JPY, GBP
  * Uses fixed conversion rates 

* **Fuel & Distance Conversion**

  * Fuel Efficiency: mpg ↔ km/L
  * Volume: Gallon ↔ Liters
  * Distance: Nautical Mile ↔ Kilometers
  * Dynamic dropdown filtering for valid conversions only

*  **Temperature Conversion**

  * Celsius ↔ Fahrenheit ↔ Kelvin
  * Uses standard scientific formulas

* **Navigation**

  * Bottom Navigation View with Fragments
  * Separate screens for each conversion category

---

## App Architecture

The application is built using a **fragment-based architecture**:

* `MainActivity`
  Handles navigation using BottomNavigationView and dynamically loads fragments.

* `CurrencyFragment`
  Handles currency conversions using a base-unit approach (USD).

* `FuelFragment`
  Handles fuel, volume, and distance conversions with category-based filtering.

* `TemperatureFragment`
  Handles temperature conversions using Celsius as the base unit.

---


## Validation & Error Handling

The app includes robust validation to ensure reliability:

* Prevents empty input
* Handles non-numeric values
* Prevents negative values (for fuel)
* Handles identity conversions (same unit selected)
* Avoids invalid unit combinations (fuel categories)

---

## Demonstration

*https://www.youtube.com/watch?v=IPZWw4Zg9V0&feature=youtu.be*

---

## Author

**Binara Lokuliyanage**
s224005681
Master of Information Technology
Deakin University

---

## License

This project is developed for academic purposes.
