# NaariRakshak
NaariRakshak is a mobile application designed to enhance women's safety and healthcare accessibility. The app enables emergency alerts via WhatsApp, sends SOS email notifications, and allows users to manage emergency contacts efficiently.

# Features
Features
🔐 User Authentication
Secure login and logout using Firebase Authentication

Session management to keep users logged in until they log out manually

📍 Location Sharing via WhatsApp
Fetches the user’s current GPS location

Sends an emergency alert message with location via WhatsApp to selected relatives

Message format:
Emergency! Here is my location: 
https://www.google.com/maps/search/?api=1&query=LATITUDE,LONGITUDE

📧 Emergency Email Alerts
Sends SOS emails to registered relatives

Email includes a custom alert message and the user’s location

👨‍👩‍👧 Relatives Management
Add, view, and select emergency contacts

Contacts are stored in Firebase Realtime Database

Selected phone numbers can be used for WhatsApp alerts

📞 Helpline Numbers
Quick access to important helpline numbers for women’s healthcare and safety

