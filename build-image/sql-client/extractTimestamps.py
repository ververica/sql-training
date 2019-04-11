#!/usr/bin/python2.7

import sys
import time
import re
from calendar import timegm

# Parses a JSON encoded text file and extracts a time attribute
# (formatted as YYYY-mm-DDThh:MM:SSZ, e.g., 2013-01-01T00:00:00Z), converts it into a UNIX timestampn
# emits the timestamp followed by the original record.
# 
# Usage:
# ./extractTimestamps.py timeField
# 
# Parameters:
# - timeField: The name of the JSON time attribute to extract

if (len(sys.argv) < 2):
  # default speedup
  print("Usage: ./extractTimestamps.py timeField")
  sys.exit(1)
else:
  # configured speedup
  timeField = sys.argv[1]

# pattern to extract timestamp
rowtimePattern = re.compile(r"\"" + timeField + "\": \"(.*Z)\",")

for line in sys.stdin:
  match = rowtimePattern.search(line)
  if match:
    timestamp = match.group(1)
    # get seconds since epoch
    epochTime = timegm(time.strptime(timestamp, "%Y-%m-%dT%H:%M:%SZ"))

    # emit row
    sys.stdout.write(str(epochTime) + "\t" + line)
