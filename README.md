# Alcohol App 🍷🍸🥃

A modern Android application for browsing, searching, and ordering alcoholic beverages with age verification and secure authentication.

## Features

### Authentication & Security
- Multiple authentication methods
  - Email/Password login
  - Google Sign-in integration
  - Phone number verification
  - Aadhar ID verification for age confirmation
- Secure sign-out process
- Age verification to ensure 18+ users only

### Product Browsing
- Home screen with featured categories and products
- Category-based browsing experience
- Detailed product pages with descriptions and pricing
- Advanced search functionality with real-time filtering

### Shopping Experience
- Add products to cart with a single tap
- Manage cart contents (add, remove, update quantities)
- View cart total in real-time
- Order placement and tracking

### User Profile
- Complete profile management system
- Multiple delivery address support
- Order history tracking
- Personal preferences management

## Technical Implementation

### Architecture
- MVVM (Model-View-ViewModel) architecture
- Repository pattern for data access
- Single Activity with Compose navigation
- Firebase integration for authentication and data storage

### UI/UX
- Modern Material Design 3 implementation
- Custom green theme throughout the application
- Responsive layouts for different screen sizes
- Smooth animations and transitions

### Technologies Used
- Kotlin programming language
- Jetpack Compose for modern UI development
- Firebase Authentication for user management
- Firebase Firestore for data storage
- Coil for image loading
- Kotlin Coroutines and Flow for asynchronous operations

## Screenshots
<p align="center">
  <img src="https://github.com/user-attachments/assets/2a912d36-858c-49c8-9c6b-8383d5c480aa" width="200" />
  <img src="https://github.com/user-attachments/assets/15384900-f9ea-4bd9-b65c-338494e9e046" width="200" />
  <img src="https://github.com/user-attachments/assets/cb1ec800-6104-4e44-a610-356e4642292a" width="200" />
  <img src="https://github.com/user-attachments/assets/f0ee8505-a6db-4e58-add4-f3b963484bb2" width="200" />
  <img src="https://github.com/user-attachments/assets/0244918d-ccbe-4e25-9ac2-358ad6428e17" width="200" />
</p>





## Getting Started

### Prerequisites
- Android Studio Arctic Fox or newer
- JDK 11 or higher
- Android SDK 33+ (Tiramisu)
- Firebase account for authentication services

### Installation
1. Clone this repository
   ```
   git clone https://github.com/yourusername/AlcoholApp.git
   ```
2. Open the project in Android Studio
3. Create a Firebase project and add the `google-services.json` file to the app directory
4. Build and run the application

## Future Enhancements
- Implement payment gateway integration
- Add user reviews and ratings
- Enhance recommendation engine based on purchase history
- Implement push notifications for order updates


## Acknowledgments
- Thanks to the Jetpack Compose team for the modern UI toolkit
- Firebase team for robust authentication services
- All contributors who helped shape this application
