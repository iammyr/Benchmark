#!/bin/bash

if [ $# -lt 3 ]
  then
	printf "Please supply 3 arguments."
	printf "Argument #1: the full path to the directory that contains the test cases of interest." 
	printf "Example: /home/myself/benchmark/src/testcases/ (where Case123.java is stored)\n\n"
	printf "Argument #2: the base URL of the servlets that SQLMap will access."
	printf "Example: http://myexample.com/myservlets/ (from where http://myexample.com/myservlets/123 can be accessed)\n\n"
	printf "Argument #3: the full path to the sqlmap.py script."
	printf "Example: /home/myself/mysqlmapclone/sqlmap.py\n\n"
	exit 1
fi

for file in $1*
do
	echo $file
	numid=$(echo $file | sed -r 's/[^0-9]+//g')
	numid=$(echo $numid | sed -r 's/^[^1-9]*//g')
        url=$2$numid?foo=1
	echo Running SQLMap on $url ...
	echo Running SQLMap on $url ... >> results.out
	python $3 --level=5 --risk=3 --dbms=hsqldb -u $url >> results.out
	echo ...Done.
	echo ...Done. >> results.out
done

