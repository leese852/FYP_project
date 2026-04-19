import urllib.request
import json

req = urllib.request.Request("http://localhost:8080/user/login", data=b'{"userAccount":"admin","userPassword":"1"}', headers={"Content-Type":"application/json"}, method="POST")
try:
    resp = urllib.request.urlopen(req)
    print(resp.read().decode('utf-8'))
except Exception as e:
    print(e)
