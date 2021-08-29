#!/bin/bash
cd cpython
git checkout --force $1
file=./Lib/bisect.py
if test -f $file
then flag="true"
else flag="false"
fi
echo $flag