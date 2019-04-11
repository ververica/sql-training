#!/usr/bin/python2.7

import sys
import time
import re

# Script to delay the emission of text file lines as specified by a timestamp.
# Each line starts with a UNIX timestamp followed by a tab character and the record data, i.e.,
# "<timestamp>\t<record>".
# 
# Usage:
# ./delay speedup
# 
# Parameters:
# - speedup: speficies how fast records are forwarded. A speedup of 2 forwards 
#            with double of the original speed.

if (len(sys.argv) < 1):
  # default speedup
  speedup = 1.0
else:
  # configured speedup
  speedup = float(sys.argv[1])

lastTimestamp = 0

# pattern to extract timestamp
timestampPattern = re.compile(r"^(.*)\t(.*)$")

for line in sys.stdin:
  match = timestampPattern.search(line)
  if match:
    timestamp = int(match.group(1))
    record = match.group(2)

    if (lastTimestamp != 0):
      # compare timestamp to timestamp of last row
      waitTime = (timestamp - lastTimestamp)
      if (waitTime != 0):
        # wait if necessary
        time.sleep(waitTime / speedup)

    # emit row
    print(record)
    # remember timestamp of this row
    lastTimestamp = timestamp
