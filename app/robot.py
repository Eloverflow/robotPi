import keyboard
import sys
import socket
import time
import RPi.GPIO as GPIO

# Create a TCP/IP socket
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# Bind the socket to the port
server_address = ('0.0.0.0', 10000)
print >>sys.stderr, 'starting up on %s port %s' % server_address
sock.bind(server_address)

# Listen for incoming connections
sock.listen(1)

#Choice of PINS
leftSideDisable=3
leftSideForward=11
rightSideDisable=5
rightSideForward=13

#Disable warnings
GPIO.setwarnings(False)

#Configure GPIO
GPIO.setmode(GPIO.BOARD)
GPIO.setup(leftSideDisable, GPIO.OUT)
GPIO.setup(leftSideForward, GPIO.OUT)
GPIO.setup(rightSideDisable, GPIO.OUT)
GPIO.setup(rightSideForward, GPIO.OUT)

#Initial state
GPIO.output(leftSideDisable , 1)
GPIO.output(leftSideForward , 0)
GPIO.output(rightSideDisable, 1)
GPIO.output(rightSideForward, 0)

def left_side_forward(power):
    print >>sys.stderr, 'left_side_forward'
    GPIO.output(leftSideDisable, 0)
    GPIO.output(leftSideForward, 1)
    time.sleep(.5)
    GPIO.output(rightSideDisable, 1)

def right_side_forward(power):
    print >>sys.stderr, 'right_side_forward'
    GPIO.output(rightSideDisable, 0)
    GPIO.output(rightSideForward, 1)
    time.sleep(.5)
    GPIO.output(leftSideDisable, 1)

def left_side_reverse(power):
    print >>sys.stderr, 'left_side_reverse'
    GPIO.output(leftSideDisable, 0)
    GPIO.output(leftSideForward, 0)

def right_side_reverse(power):
    print >>sys.stderr, 'right_side_reverse'
    GPIO.output(rightSideDisable, 0)
    GPIO.output(rightSideForward, 0)

def stopLeft():
    GPIO.output(leftSideDisable , 1)
    GPIO.output(leftSideForward , 0)

def stopRight():
    GPIO.output(rightSideDisable, 1)
    GPIO.output(rightSideForward, 0)

data=""


timeToQuit = 0

while not timeToQuit:
    if keyboard.is_pressed('q'):
        timeToQuit = 1
    print >>sys.stderr, 'waiting for a connection'
    connection, client_address = sock.accept()
    print >>sys.stderr, 'connection from', client_address

    while not timeToQuit:
       try:
         if keyboard.is_pressed('q'):
             timeToQuit = 1
         data = connection.recv(16)
         print >>sys.stderr, 'message from', client_address
         print >>sys.stderr, data
         if (data[0] == "L"):
            left_side_forward(data[2])
         elif (data[0] == "R"):
            right_side_forward(data[2])
         elif (data[0] == "A"):
            left_side_reverse(data[2])
         elif (data[0] == "P"):
            right_side_reverse(data[2])
         elif (data[0] == "S"):
            if (data[2] == "L"):
               stopLeft()
            elif (data[2] == "R"):
               stopRight()
         elif (data == "Q"):
            print ("Quit")
            break
       except KeyboardInterrupt:
         print >>sys.stderr, 'interrupt received, stopping'
         timeToQuit = 1


    connection.close()