This small project helps in converting endomondo training plans into Garmin adapted ones.

The training plans from Endomondo have no ranges in speed zones (so for example slow run is 07:16 - 07:16). 
This results in continuous too slow/too fast alerts (it's very hard to be exact and some garmin watches are 
cycling between "in zone" and "too fast" even if it is exactly the right value.

This script converts existing tcx export files by widening the range by 1% each direction (so 
for example 07:16-07:16 becomes 07:12 - 07:20.