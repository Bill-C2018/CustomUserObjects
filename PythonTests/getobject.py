import requests
import json


data = {"objectId": "00007"}
response = requests.get("http://localhost:8081/userobject", params = data )

print(response.text)