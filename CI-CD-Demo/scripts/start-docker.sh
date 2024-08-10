#!/bin/bash
cd /home/ec2-user/app

# Stop the existing application (if any)
sudo systemctl stop CI-CD-Demo

# Start the new application
java -jar CI-CD-Demo.jar &
