#!/usr/bin/env python3
import sys
import io

input_stream = io.TextIOWrapper(sys.stdin.buffer)

for line in input_stream:
    words = line.split(",")
    for word in words:
        print("%s\t%s" % (word.strip().lower(), 1))





#!/usr/bin/env python3
import sys

current_word = None
current_count = 0

# line = "hadoop\t1\n"   mapper output read by reducer
for line in sys.stdin:
    line = line.strip()    # line = "hadoop\t1"
   
    if not line:
        continue

    word, count = line.split("\t") # word = hadoop and count = "1"
    count = int(count) # count = 1

    if current_word == word:
        current_count += count
    else:
        if current_word is not None:
            print(f"{current_word}\t{current_count}")
        current_word = word
        current_count = count

# Output the last word
if current_word is not None:
    print(f"{current_word}\t{current_count}")





hadoop jar /usr/local/hadoop/share/hadoop/tools/lib/hadoop-streaming-3.3.6.jar \
-input /python_map/test1.txt \
-output /python_map/space1 \
-mapper "python3 /home/dj001/3070_hadoop/mapper.py" \
-reducer "python3 /home/dj001/3070_hadoop/reducer.py"
