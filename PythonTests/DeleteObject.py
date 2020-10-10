import requests
import json


uri = "http://localhost:8081/deletemyobjectid/00007"

response = requests.delete(uri)

print(response.text)
