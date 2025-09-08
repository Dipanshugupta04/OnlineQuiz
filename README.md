📝 Online Quiz Web App

A online quiz platform that allows users to join quiz rooms, participate in live quizzes, and get instant scores. The application supports JWT authentication, real-time updates via WebSockets, and provides a smooth and interactive quiz-taking experience.

🚀 Features

🔐 User Authentication using JWT tokens.

🏠 Room-based Quiz Participation – users can join specific quiz rooms.

📊 Auto-Scoring System – instant score calculation at the end of each quiz.

📑 REST APIs for quiz creation, room management, and analytics.

🎨 Responsive frontend with HTML, CSS, and JavaScript.

🛠️ Tech Stack

Frontend: HTML, CSS, JavaScript
Backend: Java, Spring Boot
Database: MySQL / PostgreSQL
Authentication: JWT (JSON Web Tokens)
Real-Time Communication: WebSockets
Tools: Postman, IntelliJ IDEA, GitHub

📂 Project Structure
Online-Quiz-WebApp/
│── backend/              # Spring Boot backend
│   ├── controllers/      # REST API controllers
│   ├── models/           # Entities (Quiz, Question, User, Room)
│   ├── services/         # Business logic
│   └── repository/       # JPA repositories
│
│── frontend/             # Static frontend (HTML, CSS, JS)
│   ├── index.html        # Landing page
│   ├── quiz.html         # Quiz page
│   ├── results.html      # Results display
│   └── js/               # Client-side logic
│
└── README.md             # Documentation

⚙️ Installation & Setup
1️⃣ Clone the repository
git clone https://github.com/your-username/online-quiz-webapp.git
cd online-quiz-webapp

2️⃣ Backend Setup (Spring Boot)

Import the project in IntelliJ IDEA / Eclipse.

Configure database in application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/quizdb
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update


Run the Spring Boot application:

mvn spring-boot:run

3️⃣ Frontend Setup

Open frontend/index.html in your browser, OR

Serve it with a live server (e.g., VS Code Live Server).

📌 API Endpoints (Sample)

POST /api/auth/login → User login

POST /api/quiz/create → Create new quiz

POST /api/room/join → Join a quiz room

GET /api/quiz/{id} → Fetch quiz by ID

GET /api/quiz/{id}/results → Get quiz results

🎯 Future Enhancements

Add admin dashboard to manage quizzes.

Support multiple question types (MCQ, True/False, Short Answer).

Implement leaderboards for competitive quizzes.

Deploy app on Render / AWS / Heroku.

👨‍💻 Author

Dipanshu 
📧 dipanshuguptamitrc@gmail.com

🔗 GitHub
 | LinkedIn


#QuizWiz - online quiz application

 
 
 To build docker image docker build -t username/image_name .



 Docker build and push on the docker hub and commmand for run on the arm to amd -
docker buildx build --platform linux/amd64 \
  -t docker.io/dipanshu04/quizwiz:latest \
  --push .
 
