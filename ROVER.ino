#include <Smartcar.h>

Car car;

SR04 ultrasonicSensor;
const int TRIGGER_PIN = 6; //D6
const int ECHO_PIN = 5; //D5
int spd = 50;
int leftAngle = -90;
int rightAngle = 90;

void setup() {
  Serial3.begin(9600);
  Serial3.setTimeout(200);
  car.begin();

  ultrasonicSensor.attach(TRIGGER_PIN, ECHO_PIN);
}

void loop() {
 int dist = ultrasonicSensor.getDistance();
 if (dist > 0 && dist < 15){
    car.setSpeed(0);
        
      
  }
  handleInput();
}

void handleInput() { //handle Serial3 input if there is any
  if (Serial3.available()) {
    String input = Serial3.readStringUntil('\n');
    if (input.equals("m")) {
      car.setSpeed(spd);
      car.setAngle(0);
    }else if (input.equals("l")){
      
      car.setAngle(leftAngle);
    }
    else if (input.equals("r")){
     car.setAngle(rightAngle); 
    }
    else if (input.equals("b")){
      car.setSpeed(0-spd); 
    }
    else if (input.equals("s")){
     car.setSpeed(0); 
    }
   }
  }

