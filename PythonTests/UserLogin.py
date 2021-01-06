import requests
import json

data = {
    "userName": "bob",
    "userPword": "larry",
    }


response = requests.post("http://localhost:8081/user/login", json = data )

print(response.status_code)
print(response.text)