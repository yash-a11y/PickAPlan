


# PickAPlan - Subscription Management App

A mobile application for managing subscription plans with integrated Stripe payment functionality.

## Prerequisites

- Java JDK 8+
- Android Studio
- Stripe account (https://stripe.com)
- Backend API (https://github.com/yash-a11y/apiCentreal/tree/master)

## Installation & Setup

### Android application Setup
1. Clone the repository's release branch:
   ```bash
   git clone -b final_release_v1.0 https://github.com/your-username/PickAPlan.git
   ```
2. Configure Stripe Keys:
   - Navigate to:
     ```
     /PickAPlan/app/src/main/java/com/example/pickaplan/adapter/plansAdapter.java
     ```
   - Replace `"Your publishable key"` with your Stripe publishable key

### Backend Setup
1. Clone the backend repository:
   ```bash
   git clone https://github.com/yash-a11y/apiCentreal.git
   ```
2. Configure Stripe Secret:
   - Create `application.properties` in `src/main/resources/`
   - Add:
     ```properties
     stripe.secret.key=YOUR_STRIPE_SECRET_KEY
     ```
3. Run the backend server

## System Architecture

![System Design Diagram](https://raw.githubusercontent.com/yash-a11y/PickAPlan/master/systemdesign.png)


Components:
- **Frontend**: Android Java application for user interactions
- **Backend**: Spring Boot server handling payment processing and telecome plans fetching
- **Redis**: Caching scraped data, reduce latency
- **Firebase**: user authentication and store user's data
- **Stripe**: Payment processing and subscription management

## Payment Setup Instructions

1. Create a Stripe account at https://dashboard.stripe.com/register
2. Get your API keys from Stripe Dashboard:
   - Publishable key (for Android client)
   - Secret key (for backend server)
3. Update both applications:
   - Frontend: `plansAdapter.java` → Publishable key
   - Backend: `application.properties` → Secret key

## Important Notes

- Never commit API keys to version control
- Keep backend server running for payment functionality
- Test payments using Stripe test mode credentials

## Contributing

Contributions welcome! Please fork the repository and submit pull requests.
