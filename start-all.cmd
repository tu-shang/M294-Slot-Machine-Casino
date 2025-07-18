@echo off
start "" "C:\Program Files\MySQL\MySQL Workbench 8.0\MySQLWorkbench.exe" -query projekt
start cmd /k "cd backend && mvnw spring-boot:run"
start cmd /k "cd frontend\frontend && npm run dev"

timeout /t 5 >nul // keine ausgabe im Terminal 
start "" http://localhost:5173/
