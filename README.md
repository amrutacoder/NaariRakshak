NaariRakshak 👩‍⚕️🚑
A mobile application designed to enhance women's safety and healthcare accessibility by enabling emergency alerts, WhatsApp SOS messages, email notifications, and relative contacts management.

Features ✨
🔐 User Authentication
Firebase Authentication for secure login/logout.
Session management ensures users stay logged in until they log out manually.

📍 Location Sharing via WhatsApp
Fetches the user’s current GPS location.
Sends an emergency alert message with location via WhatsApp to selected relatives.
WhatsApp message format:
Emergency! Here is my location: https://www.google.com/maps/search/?api=1&query=LATITUDE,LONGITUDE

📧 Emergency Email Alerts
Users can send emergency emails to their registered relatives.
The email includes a customized alert message along with the user's location.

👨‍👩‍👧 Relatives Management
Users can add, view, and select relatives from Firebase Database.
Phone numbers stored in Firebase can be selected for emergency WhatsApp messages.

📞 Helpline Numbers
Quick access to important helpline numbers related to women’s healthcare and safety.
🏠 Home & Navigation
Easy-to-use dashboard with all important actions accessible via buttons.

Technologies Used 🛠️
Android (Java & XML)
Firebase Authentication (for login & session management)
Firebase Realtime Database (to store relatives' contact details)
Google Maps API (for fetching user location)
WhatsApp Intent API (to send emergency messages)
Email API (to send emergency alerts)

Installation & Setup 🚀

git clone https://github.com/akashgaikwad-cmd/NaariRakshak.git
cd NaariRakshak
Open in Android Studio
Add Firebase Configuration
Go to Firebase Console → Create a new project.
Download google-services.json and place it in the app/ directory.
Run the app on your device! 🎉

Screenshots 📸
<p align="center">
<img src="https://github.com/akashgaikwad-cmd/NaariRakshak/blob/main/outputs/IMG-20250310-WA0031.jpg" alt="Image description" width="300" height="500">
<img src="https://github.com/akashgaikwad-cmd/NaariRakshak/blob/main/outputs/IMG-20250310-WA0032.jpg" alt="Image description" width="300" height="500">
<img src="https://github.com/akashgaikwad-cmd/NaariRakshak/blob/main/outputs/IMG-20250310-WA0033.jpg" alt="Image description" width="300" height="500">
<img src="https://github.com/akashgaikwad-cmd/NaariRakshak/blob/main/outputs/IMG-20250310-WA0034.jpg" alt="Image description" width="300" height="500">
</p>
<p align="center">
<img src="https://github.com/akashgaikwad-cmd/NaariRakshak/blob/main/outputs/IMG-20250310-WA0035.jpg" alt="Image description" width="300" height="500">
<img src="https://github.com/akashgaikwad-cmd/NaariRakshak/blob/main/outputs/IMG-20250310-WA0036.jpg" alt="Image description" width="300" height="500">
<img src="https://github.com/akashgaikwad-cmd/NaariRakshak/blob/main/outputs/IMG-20250310-WA0037.jpg" alt="Image description" width="300" height="500">
<img src="https://github.com/akashgaikwad-cmd/NaariRakshak/blob/main/outputs/IMG-20250310-WA0038.jpg" alt="Image description" width="300" height="500">
</p>
<p align="center">
<img src="https://github.com/akashgaikwad-cmd/NaariRakshak/blob/main/outputs/IMG-20250310-WA0039.jpg" alt="Image description" width="300" height="500">
<img src="https://github.com/akashgaikwad-cmd/NaariRakshak/blob/main/outputs/IMG-20250310-WA0040.jpg" alt="Image description" width="300" height="500">
</p>

Contributors 🤝
Akash Gaikwad (Developer)

License 📜
This project is open-source and available under the MIT License.
