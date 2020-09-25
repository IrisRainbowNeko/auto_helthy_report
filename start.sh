export DISPLAY=":99"
start-stop-daemon --start --background --user root --exec "/usr/bin/sudo" -- -u root /usr/bin/Xvfb :99 -screen 0 1024x768x24
sleep 3
java -jar ~/rep/auto_report.jar