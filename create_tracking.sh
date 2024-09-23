#!/bin/bash

curl -X POST http://localhost:8080/api/packages \
     -H "Content-Type: application/json" \
     -d '{"trackingNumber": "TRACK001", "status": "Pending"}'


