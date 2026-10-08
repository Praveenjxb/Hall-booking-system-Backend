$ErrorActionPreference = "Stop"

Write-Host "1. Testing GET /users?email=praveenuser@gmail.com"
$existing = Invoke-RestMethod -Uri "http://localhost:8080/users?email=praveenuser@gmail.com"
Write-Host "Existing count: $($existing.Count)"

Write-Host "2. Testing POST /users (Register user praveen01)"
$body = '{"name":"praveen01","email":"praveenuser@gmail.com","phone":"1234567890","password":"User@123","role":"user"}'
$created = Invoke-RestMethod -Uri "http://localhost:8080/users" -Method Post -Body $body -ContentType "application/json"
Write-Host "Created User: ID=$($created.id), Name=$($created.name), Email=$($created.email), Role=$($created.role)"

Write-Host "3. Testing Login for praveenuser@gmail.com"
$loginRes = Invoke-RestMethod -Uri "http://localhost:8080/users?email=praveenuser@gmail.com&password=User@123"
Write-Host "Login Verified: ID=$($loginRes[0].id), Name=$($loginRes[0].name)"

Write-Host "Registration & Login Test SUCCESSFUL!" -ForegroundColor Green
