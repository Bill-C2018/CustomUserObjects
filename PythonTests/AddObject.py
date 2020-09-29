import requests
import json

data = {
    "myObjectId": "00007",
    "rightAcension": "19:23:15",
    "declination": "-23:43:19",
    "otherCatalogueId": "SAO 12345",
    "description": "my  object",
    "type": "bob"
    }


response = requests.post("http://localhost:8081/submitobject", json = data )

print(response.text)

