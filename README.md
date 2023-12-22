# Mobile Development Documentation

## Architecture: MVVM (Model-View-ViewModel)
![image](https://github.com/FitNest-AI/Mobile-Development-App/assets/52656052/f8bb1bdd-0896-4e5c-977b-43bc9706b217)


## Features:

### 1. Splash Screen
- Description: The app's initial screen, providing a welcoming interface for users.
  
### 2. Login and Create Account Screen
- Description: Allows users to log in to their existing accounts or create new ones.
  
### 3. Home Screen
- Description: Displays buttons for actions like discovering workout sets, creating an workout set, and shows several recommended workout sets based on user preferences with machine learning model.

### 4. Workout Set List Screen
- Description: Lists all recommended workout sets and there search bar also.

### 5. Recommended Food Screen
- Description: Displays recommended food options based on user preferences with machine learning model.

### 6. Profile Screen
- Description: Displays the user's profile information.

### 7. Camera with Integration of Machine Learning Model (Pose Estimation)
- Description: Integrates a camera feature with a machine learning model for pose estimation.

## Dependencies:

- Retrofit:
  ```groovy
  implementation("com.squareup.retrofit2:retrofit:2.9.0")
  implementation("com.squareup.retrofit2:converter-gson:2.9.0")
  ```

- Okhttp:
  ```groovy
  implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
  ```

- Firebase Auth:
  ```groovy
  implementation("com.google.firebase:firebase-auth:22.3.0")
  ```

- TensorFlow Lite:
  ```groovy
  implementation("org.tensorflow:tensorflow-lite-support:0.3.0")
  implementation("org.tensorflow:tensorflow-lite-metadata:0.1.0")
  implementation("org.tensorflow:tensorflow-lite-gpu:2.5.0")
  ```

- Circle Image:
  ```groovy
  implementation("de.hdodenhof:circleimageview:3.1.0")
  ```

- Circular Progress View:
  ```groovy
  implementation("com.github.antonKozyriatskyi:CircularProgressIndicator:1.3.0")
  implementation("com.github.guilhe:circular-progress-view:2.0.0")
  ```

## Tools:

- Android Studio
- JRE (Java Runtime Environment) or JDK (Java Development Kit)

## Installation:

1. Clone this repository from GitHub:
   [GitHub Repository](https://github.com/FitNest-AI/Mobile-Development-App.git)

2. Import the cloned repository into Android Studio for further development.

