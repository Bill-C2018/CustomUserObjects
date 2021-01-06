import requests
import json


data = {"filter": "other"}
response = requests.get("http://localhost:8081/listall/Other")

print(response.text)