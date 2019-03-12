from time import sleep
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

def right_side_forward(power):
    print >>sys.stderr, 'right_side_forward'
    GPIO.output(rightSideDisable, 0)
    GPIO.output(rightSideForward, 1)

def left_side_reverse(power):
    print >>sys.stderr, 'left_side_reverse'
    GPIO.output(leftSideDisable, 0)
    GPIO.output(leftSideForward, 0)

def right_side_reverse(power):
    print >>sys.stderr, 'right_side_reverse'
    GPIO.output(rightSideDisable, 0)
    GPIO.output(rightSideForward, 0)

def stopLeft():
    print >>sys.stderr, 'stopLeft'
    GPIO.output(leftSideDisable , 1)
    GPIO.output(leftSideForward , 0)

def stopRight():
    print >>sys.stderr, 'stopRight'
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
         data = connection.recv(2)
         print >>sys.stderr, data

         if (len(data) == 1):
            data = data + connection.recv(1)
         elif (len(data) == 2 and data[0] == "S"):
            if (data[1] == "L"):
               stopLeft()
#               connection.send(data)
            elif (data[1] == "R"):
               stopRight()
#               connection.send(data)
         elif (len(data) == 2):
            data = data + connection.recv(1)

         if (len(data) == 3):
            if (data[0] == "L" and data[1] == "F"):
               left_side_forward(data[2])
#               connection.send(data)
            elif (data[0] == "L" and data[1] == "B"):
               left_side_reverse(data[2])
#               connection.send(data)
            elif (data[0] == "R" and data[1] == "F"):
               right_side_forward(data[2])
#               connection.send(data)
            elif (data[0] == "R" and data[1] == "B"):
               right_side_reverse(data[2])
#               connection.send(data)

         sleep(0.05)

       except KeyboardInterrupt:
         print >>sys.stderr, 'interrupt received, stopping'
         timeToQuit = 1


    connection.close()
sock.shutdown(socket.SHUT_RDWR)