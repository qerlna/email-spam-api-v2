Email Spam Detection REST API (Back-end Specialization)
This project is a high-performance RESTful API developed as a Project of Specialization for the Bachelor of Engineering and Informatics degree at Ala-Too International University.

Project Overview
The system provides a programmatic way to manage emails and automatically classify them as Spam or Ham (normal) using a custom scoring algorithm. It is designed with a focus on scalability, clean architecture, and ease of integration.

Live API: https://email-spam-api-v2.onrender.com

Full Project Access: https://drive.google.com/file/d/1ZbMcSifDlRR3Oh8kbYyRllm6NS8rT5VT/view?usp=share_link

Video Presentation: https://drive.google.com/file/d/1hcpk_GAOX3tMrRCwGVCrOJ7RgYfHzKW6/view?usp=sharing


Interactive Documentation: Swagger UI

Tech Stack
Backend: Java 17, Spring Boot 3.x

Data: Spring Data JPA, PostgreSQL (Render Managed)

Documentation: Swagger UI / Open API 3.0

Testing: Postman, JUnit 5

Deployment: Render (PaaS) with CI/CD from GitHub

System Architecture
The project follows the N-tier architecture pattern to ensure separation of concerns:

Controller Layer: Handles HTTP requests and maps them to service methods.

Service Layer: Contains the core business logic (Spam scoring algorithm).

Repository Layer: Manages data persistence with PostgreSQL.

Security Layer: Implements authentication and authorization logic.

API Features & Documentation
You can test the API functionality directly via the Swagger UI.

Key Endpoints:

POST /api/email/check-spam — Analyzes text and returns a spam classification result.

GET /api/email/history — Retrieves a full history of checked messages from the database.

GET /api/email/history/spam — Filters and shows only spam-detected messages.

Request Example (JSON):

JSON
{
  "text": "FREE PRIZE! Click here to claim your $1000 gift card now!"
}
Response Example:

JSON
{
  "text": "FREE PRIZE! Click here to claim your $1000 gift card now!",
  "isSpam": true,
  "spamScore": 8.5,
  "status": "SPAM"
}
<img width="1280" height="800" alt="Screenshot 2026-04-29 at 18 57 34" src="https://github.com/user-attachments/assets/dd1dc8e8-83c3-4da4-badd-4d10d17d9814" />
The figure demonstrates the API processing a suspicious email text. The system successfully identified spam triggers and returned a classification result with a high spam score.

<img width="1280" height="800" alt="Screenshot 2026-04-29 at 18 59 07" src="https://github.com/user-attachments/assets/08c9e469-b576-4c32-80e6-0420b0b4c996" />
This view shows the integration with the PostgreSQL database. Every processed request is logged and can be retrieved for further analysis or auditing

<img width="1280" height="800" alt="Screenshot 2026-04-29 at 19 00 04" src="https://github.com/user-attachments/assets/94816514-1de9-4124-8688-4b351f18ada7" />



Author

Naizabekova Aigerim Group: Com22

Ala-Too International University

Supervisor: Dr. Tauheed Khan
