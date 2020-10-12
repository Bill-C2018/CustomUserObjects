import requests
import json


data = {"objectId": "00007"}
response = requests.get("http://localhost:8081/userobject", params = data )

print(response.text)
jstring = response.text
tobj = json.loads(jstring)
customobj = tobj['objects'][0]
print (customobj)
idt = customobj['otherCatalogueId']
print (idt)
customobj['otherCatalogueId'] = "SAO 54321"

response = requests.post("http://localhost:8081/editobject", json = customobj )

print(response.text)

response = requests.get("http://localhost:8081/userobject", params = data )

print(response.text)