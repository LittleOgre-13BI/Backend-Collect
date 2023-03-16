#!/bin/sh
echo "********************************************************"
echo "Starting Similarity Server"
echo "********************************************************"
docker rm -f similarity
docker run -d -v /home/ubuntu/similarity-service:/similarity-service -p 8082:5000 --name="similarity" python-similarity:v1 /bin/sh -c 'cd /similarity-service && python app.py'
