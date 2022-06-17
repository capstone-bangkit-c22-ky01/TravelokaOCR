## TravelokaOCR Mobile Development Documentation
Here is the official repository of Traveloka OCR Android App project. In this project we use XML for app layout and Kotlin to develop the application in order to complete the **[Traveloka Singapore](https://www.traveloka.com/en-sg/) x [Bangkit Academy led by Google, Tokopedia, Gojek, & Traveloka](https://grow.google/intl/id_id/bangkit/)** Company Capstone Project.

  - ### Architecture for this project
  - ### Features
      - ##### **Splash Screen**, There is animation before entering the login page
      - ##### **Login**, Allows a user to have an access to the application by entering their username and password
      - ##### **SignUp**, Enables users to register and have an access to the application
      - ##### **Home/Flight Screen**, The starting page that is displayed when you have logged in on your device. It will allow user to enter their departure, destination, date, number of passenger, and airline classes and let the user to submit their airline details
      - ##### **Flight Result Screen**, This page will display the list of tickets after user pressing submit button in the previous page and allow user to pick a ticket 
      - ##### **Dialog Scanning Option**, This dialog will be displayed after the user pick the ticket and options will appeared weather the user choose to automatically scan or manually input their ID card information
      - ##### **Dialog Scanning Option**, This dialog will be displayed after the user pick the ticket and options will appeared weather the user choose to automatically scan (Continue) or manually input their ID card information (No, let me fill in manually)   
      - ##### **OCR Screen**, This page will be displayed after the user choose to continue from the previous dialog. It will allow user to automatically retrieve their personal information by adjusting their ID card position in bonding box 
      - ##### **Scan Result Verification Screen**, This page will be displayed after the user successfully scan their ID card. It will allow user to verify their information and give an option to rescan their ID card if needed. Then the user need to tap the submit button   
      - ##### **Success Information Screen**, This page will be displayed after the user successfully scan their ID card. User also can go to history page or buy another ticket
      -  ##### **History Screen**, The users will be able to see their ticket history here. They also can delete all the history by pressing delete icon
      -  ##### **History Detail Screen**, The users will be able to see their ticket history detail here. They also can delete selected history by pressing delete icon
      -  ##### **Profile Screen**, The users will be able to access their profile here. They can edit their profile by pressing edit profile button or logout from the account by pressing Logout button
      -  ##### **Edit Profile Screen**, The users will be able to edit their profile such as editing profile picture, username, and email
  - #### Dependencies
      - ##### [Lifecycle and Live Data](https://developer.android.com/jetpack/androidx/releases/lifecycle)
      - ##### [Navigation Component](https://developer.android.com/jetpack/androidx/releases/navigation)
      - ##### [Retrofit 2](https://square.github.io/retrofit/)
      - ##### [CameraX](https://developer.android.com/training/camerax)
      - ##### [TFLite](https://www.tensorflow.org/lite/android/quickstart)
      - ##### [Circle Image](https://github.com/hdodenhof/CircleImageView)
      - ##### [Glide]([https://github.com/hdodenhof/CircleImageView](https://github.com/bumptech/glide)

### Getting Started Application
  - ### Prerequisites
      - ##### Tools Sofware
        - [Android Studio](https://developer.android.com/studio)
        - JRE (Java Runtime Environment) or JDK (Java Development Kit).
  - #### Installation
      - Clone this repository and import into Android Studio    
          ```
             https://github.com/capstone-bangkit-c22-ky01/TravelokaOCR.git
             
  ## References
  * [Clean Architecture Guide](https://developer.android.com/jetpack/guide)
  * [Android Application Fundamental](https://developer.android.com/guide/components/fundamentals)
  * [Android Jetpack Pro](https://developer.android.com/jetpack)
  * [Dependency injection](https://developer.android.com/training/dependency-injection)
