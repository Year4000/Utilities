#!/bin/bash

# Git Config User
git config --global user.email "year4000@year4000.net"
git config --global user.name "Year4000"

# FireCast Minecraft Server
git clone https://github.com/Year4000/FireCast.git
cd FireCast/
./init.sh
./build.sh
cd ../
