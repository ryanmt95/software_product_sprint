#!/bin/bash
gcloud config set project rtan-sps-summer20
cd portfolio/src/main/webapp/assets/css/
npm install -g sass
sass --watch hobbies.scss:hobbies.css