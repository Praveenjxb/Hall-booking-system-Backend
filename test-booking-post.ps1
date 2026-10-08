try {
    $body = '{"date":"2026-11-20","slot":"Morning","eventType":"Wedding","guests":150,"purpose":"Reception","hallId":1,"userId":2,"status":"pending","createdAt":"2026-09-30T12:00:00.000Z"}'
    $res = Invoke-RestMethod -Uri "http://localhost:8080/bookings" -Method Post -Body $body -ContentType "application/json"
    Write-Host "SUCCESS: $($res | ConvertTo-Json -Compress)"
} catch {
    Write-Host "FAILED!"
    if ($_.Exception.Response) {
        Write-Host "HTTP Status: $([int]$_.Exception.Response.StatusCode)"
        $stream = $_.Exception.Response.GetResponseStream()
        $reader = New-Object System.IO.StreamReader($stream)
        Write-Host "Response Body: $($reader.ReadToEnd())"
    } else {
        Write-Host "Exception: $($_.Exception.Message)"
    }
}
