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

[Include screenshots of key screens here]

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

## License
This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments
- Thanks to the Jetpack Compose team for the modern UI toolkit
- Firebase team for robust authentication services
- All contributors who helped shape this application
