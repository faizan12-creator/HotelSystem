# ##🏨 Grand Horizon — Hotel Management System
A professional JavaFX-based Hotel Management System with role-based access, booking management, room service, feedback, and revenue reporting.

**📸 Screenshots**

Admin Dashboard · Guest Portal · Payment · Receipt


# ##✨ Features
🔐 Authentication

Login & Register system
Role-based access: Admin and Guest
Persistent user storage

**👨‍💼 Admin Panel**

Full reservation management (Book, Check Out, Cancel)
Process payments (Cash, Card, EasyPaisa, JazzCash, IBFT)
Generate receipts
View all guest feedback & ratings
Manage room service orders
Room availability calendar
Revenue reports & charts (by room type, payment method, monthly trend)
Save & load database

# ##👤 Guest Portal

Book rooms (Standard, Deluxe, Suite, Penthouse)
View personal reservations
Process payments
View receipts
Submit star ratings & feedback
Order room service from menu
View room availability calendar


# ##🛏️ Room Types & Rates
Room TypeRate per Night 
|StandardRs 5,000|
|DeluxeRs 9,000|
|SuiteRs 15,000|
|PenthouseRs 25,000|
# ##🗂️ Project Structure
src/
└── HotelSystem/
    ├── Main.java               # App entry point
    ├── AppContext.java         # Shared state & session
    ├── Booking.java            # Booking data model
    ├── Feedback.java           # Feedback data model
    ├── RoomServiceOrder.java   # Room service order model
    ├── DatabaseManager.java    # File I/O (save/load)
    ├── AuthController.java     # Login & register logic
    ├── BookingController.java  # Booking CRUD operations
    ├── UIHelper.java           # Shared UI factory methods
    ├── LoginView.java          # Login screen
    ├── RegisterView.java       # Register screen
    ├── AdminView.java          # Admin dashboard
    ├── UserView.java           # Guest portal
    ├── PaymentView.java        # Payment dialog
    ├── ReceiptView.java        # Receipt dialog
    ├── FeedbackView.java       # Feedback submit & admin view
    ├── RoomServiceView.java    # Room service ordering
    ├── RoomCalendarView.java   # Room availability calendar
    └── ReportsView.java        # Revenue reports & charts
  # ##  🚀 Getting Started
# ##Prerequisites

1)Java JDK 17 or higher
2)JavaFX SDK 17 or higher
3)IntelliJ IDEA (recommended)

**Setup in IntelliJ IDEA**
1. Clone the repository
bashgit clone https://github.com/faizan12-creator/HotelSystem.git
2. Open in IntelliJ IDEA
File → Open → Select the project folder
3. Add JavaFX SDK

Download JavaFX SDK from gluonhq.com
Go to File → Project Structure → Libraries → +
Add the JavaFX lib folder

4. Configure Run

Go to Run → Edit Configurations
Set Main class: HotelSystem.Main
Add VM options:

--module-path "path/to/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml
5. Run the project
Shift + F10

**🔑 Default Credentials**
RoleUsername: Adminadmin/Adminmanager/staff/guest
Password:admin123/mgr456/staff789/guest000

New accounts can be registered directly from the login screen.


**💳 Payment Methods**

💵 Cash Payment
💳 Credit / Debit Card
📱 Online Transfer / IBFT
📲 EasyPaisa
📲 JazzCash


**🛠️ Tech Stack**
TechnologyUsageJava 17+Core languageJavaFXUI FrameworkJava SerializationData persistenceCSS (JavaFX inline)Styling & theming

**📊 Admin Reports Include**

Revenue by room type (bar chart)
Bookings overview (confirmed, checked out, cancelled)
Payment method breakdown (pie chart)
Monthly revenue trend
Recent bookings list


# ##👨‍💻 Developer 
**Faizan**

GitHub: @faizan12-creator


📄 License
This project is for educational purposes.
