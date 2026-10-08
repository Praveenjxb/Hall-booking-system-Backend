$ErrorActionPreference = "Stop"

Write-Host "--- 1. Testing GET /users ---"
$users = Invoke-RestMethod -Uri "http://localhost:8080/users"
Write-Host "Total users: $($users.Count)"

Write-Host "--- 2. Testing GET /halls ---"
$halls = Invoke-RestMethod -Uri "http://localhost:8080/halls"
Write-Host "Total halls: $($halls.Count)"

Write-Host "--- 3. Testing GET /bookings ---"
$bookings = Invoke-RestMethod -Uri "http://localhost:8080/bookings"
Write-Host "Total bookings: $($bookings.Count)"

Write-Host "--- 4. Testing POST /auth/login ---"
$loginBody = '{"email":"admin@hallbook.com","password":"Admin@123"}'
$loginRes = Invoke-RestMethod -Uri "http://localhost:8080/auth/login" -Method Post -Body $loginBody -ContentType "application/json"
Write-Host "Login response: $($loginRes | ConvertTo-Json -Compress)"

Write-Host "--- 5. Testing POST /bookings (New Booking) ---"
$newBooking = '{"hallId":1,"userId":2,"date":"2026-11-15","slot":"Morning","eventType":"Seminar","guests":100,"purpose":"Tech Summit"}'
$createdBooking = Invoke-RestMethod -Uri "http://localhost:8080/bookings" -Method Post -Body $newBooking -ContentType "application/json"
Write-Host "Created Booking ID: $($createdBooking.id), Status: $($createdBooking.status)"

Write-Host "All tests PASSED successfully!" -ForegroundColor Green
