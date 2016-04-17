#include <Smartcar.h>

Car car;

SR04 ultrasonicSensor;
// Front sensor pins
const int TRIGGER_PIN = 6;
const int ECHO_PIN = 5;
int spd = 50;
// Back sensor pins
int leftAngle = -90;
int rightAngle = 90;
int last = 0;
int old = 0;

SR04 ultrasonicSensor2;
const int PIN_1 = 10;
const int PIN_2 = 9;

bool forward = true;
void setup() {
  Serial3.begin(9600);
  Serial3.setTimeout(200);
  car.begin();

  ultrasonicSensor.attach(TRIGGER_PIN, ECHO_PIN);
  ultrasonicSensor2.attach(PIN_1, PIN_2);
}

void loop() {
  if(forward){
   int dist = ultrasonicSensor.getDistance();
   if (dist > 0 && dist < 22 && (old-last) < 10 && (last-dist) < 10 && last > 0 && old > 0){
      car.setSpeed(0);

   }
   old = last;
   last = dist;
  }
  else{
   
   int dist = ultrasonicSensor2.getDistance();
   if (dist > 0 && dist < 22 && (old-last) < 10 && (last-dist) < 10 && last > 0 && old > 0){
    car.setSpeed(0); 
   }
    old = last;
    last = dist;
   }
   handleInput();
  }


void handleInput() { //handle Serial3 input if there is any
  if (Serial3.available()) {
    String input = Serial3.readStringUntil('\n');
    if (input.equals("m")) {
      car.setSpeed(spd);
      car.setAngle(0);
      forward = true;
      last = 0;
      old = 0;
    }else if (input.equals("l")){
      
      car.setAngle(leftAngle);
    }
    else if (input.equals("r")){
     car.setAngle(rightAngle); 
    }
    else if (input.equals("b")){
      car.setSpeed(0-spd); 
      forward = false;
      old = 0;
      last = 0;
    }
    else if (input.equals("s")){
     car.setSpeed(0);
    }
   }
  }
