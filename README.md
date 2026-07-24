netstat -ano | findstr :8081
tasklist | findstr 3992 

taskkill /PID 3992 /F
