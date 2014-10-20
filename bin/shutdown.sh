#! /bin/sh

sudo pkill -f 'boxupp'
echo "BoxUpp shutdown complete"
if [ $? -ne 0 ]; then
    echo "Error stopping BoxUpp Please check logs"
    exit 1
fi



