# Android APP conected to REST Service

This application, designed for Android devices, has been developed using Android Studio. Integrated with a RESTful database initiated on a Linux Spring Boot platform, users can seamlessly interact with various gym-related functionalities, including user registration, classroom reservations, workout recommendations, BMI calculation, gym news updates, and secure PayPal-based payments. For further details about the database, you can visit https://github.com/jmgalin/gym-REST.


## Installing

 - The principal files are here

 - Start psql and create database with automated script:
 
 `restartBBDD.sh`

  - Start spring-boot in terminal situated in proyect folder:
 
 `mvn spring-boot:run`

  - Start in Android Studio with the proyect folder

 # Functions avaliable included in App:

 - Authenticate using 'username' and 'password':

 --/login?usuario=[username]&contrasena=[password]

 - Access the latest news feed:

 --/noticias

 - Register with 'username', 'email', and 'password':

 --/registro?usuario=[username]&email=[email]&contrasena=[password]

 - Reserve a class with 'username' and 'class_id':

 --/reservas/reservaaula?usuario=[username]&id=[class_id]

 - View reservations for 'username':

 --/verreservas?usuario=[username]

 # App description:

 LOG IN AND USER REGISTRATION
 Users enter their username and password in the login section, establishing a connection with the database to verify their registration and validate the provided information. In the registration segment, users can sign up by entering their username, email, password, and confirming the password to ensure accuracy and uniqueness of user details.
 
 ![Initial Menu](/img/imginit.png)

 MAIN APPLICATION MENU
 The main application menu displays various options including gym news, a timer for suggested workouts, classroom reservations, list of reservations, BMI calculator, and PayPal payments. It greets the user and offers a "logout" button for exiting the application.

 ![Main Menu](/img/imgmenu.png)

 CLASSROOM RESERVATIONS AND LISTING RESERVATIONS MADE
 Users can view available classes or rooms, the instructing teacher, location, available seats, and the date and time of the class. To confirm a reservation, users must provide confirmation. Moreover, users cannot make duplicate reservations for the same class.

 ![Reservation Menu](/img/imgreservation.png)
 
 GYM NEWS
 This section allows users to access the most relevant gym news, ordered by publication date. The data is retrieved from the system's database through an API request.

 ![News Menu](/img/imgnotice.png)

 RECOMMENDED WORKOUTS
 Here, users can view the recommended workouts, including descriptions detailing the specific body areas targeted and the repetition count for each exercise. Initiating a workout starts a timer indicating the time remaining for a specific exercise repetition.

 ![Workout Menu](/img/imgtimer.png)

 BMI CALCULATOR
 This function allows users to calculate their Body Mass Index (BMI) using weight in kilograms and height in meters. The calculator displays the calculated index and offers a description based on the computed value.

 ![BMI Menu](/img/imageimccalculator.png)

 PAYMENTS VIA PAYPAL
 Users can pay the gym membership fee using the REST service of PayPal, redirecting them to PayPal's payment platform. Upon entering credentials and confirming the purchase, users receive a purchase ID, finalizing the payment and returning to the main menu.
 
 ![Paypal Payments](/img/imgpaypalpayment.png)