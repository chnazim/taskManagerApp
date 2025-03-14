# taskManagerApp
A simple yet powerful Task Manager App built using Kotlin to help users manage tasks efficiently

📌 Features
✅ Task Management: Create, edit, delete, and mark tasks as completed.
✅ Due Date Selection: Choose task deadlines with a date picker.
✅ Sorting & Filtering: Sort tasks by Priority, Due Date, and Alphabetically.
✅ Task Status Filtering: View All, Completed, or Pending tasks.
✅ Swipe-to-Delete: Swipe left to remove tasks smoothly.
✅ Animations & Material 3 UI: Modern UI with dark mode support.

🛠 Tech Stack
📱 UI: Jetpack Compose (Material 3)
🗄️ Storage: Room Database (Persistence)
⚙️ DI: Hilt (Dependency Injection)
📌 Navigation: Jetpack Navigation Compose
🔄 State Management: ViewModel + LiveData
📝 Testing: UI Tests with Compose Testing

🚀 Getting Started
🔹 Prerequisites
Android Studio Giraffe | 2023.3.1+
Android device/emulator running API Level 24+
Gradle 8.1+

🔹 Clone the Repository
git clone https://github.com/yourusername/taskManagerApp.git
cd taskManagerApp

🔹 Open in Android Studio
Open Android Studio and select the ttaskManagerApp project.
Click Run > Run 'app' or press Shift + F10.

📌 How to Use
1️⃣ Add a Task
Click on the ➕ FAB (Floating Action Button).
Enter the task title, description, priority, and due date.
Click Save Task.
2️⃣ Update/Delete a Task
Tap on a task to view details.
Swipe left on a task to delete it.
3️⃣ Sorting & Filtering
Use the Sort button (🔀) to sort by priority, due date, or alphabetically.
Use the Filter button (🔎) to filter tasks by All, Completed, or Pending.
