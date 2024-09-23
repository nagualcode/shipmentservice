#!/bin/bash

for dir in *; do
	if cd "$dir" 2>/dev/null; then
		if ls pom.xml 2>/dev/null; then
			pwd
			if ! mvn clean install; then echo "error"; exit 1; fi
			ls target
		fi
		cd ..
	fi
done
find . -name *.jar -type f -exec stat --format='%n %w' {} \; 
