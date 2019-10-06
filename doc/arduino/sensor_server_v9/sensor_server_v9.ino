//LCD
#include <LiquidCrystal.h>

const int rs = 8, en = 9, d4 = 4, d5 = 5, d6 = 6, d7 = 7;
LiquidCrystal lcd(rs, en, d4, d5, d6, d7);

//BUZZER
#define BUZZER_PIN 22
#define DELAY_BUZZ_EN_MILLI 2000
#define TIEMPO_ALARMA_BUZZ 7500

//Server
#include <ArduinoJson.h>
#include <Ethernet.h>
#include <SPI.h>
#include <DHT.h>

#define DHTPIN 30//31
#define DHTTYPE DHT11

IPAddress ip(192, 168, 2, 213);
IPAddress myDns(192,168,0, 1);
IPAddress gateway(192, 168, 0, 1);
IPAddress subnet(255, 255, 255, 0);
byte mac[] = {0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED};
EthernetServer server(80);

//Movimiento
#define MOVIMIENTO_PIN_IN 24

//Temperatura
DHT dht(DHTPIN, DHTTYPE);

//Teclado
const byte ROWS = 4;
const byte COLS = 4;

char keys[ROWS][COLS] = {
  {'D','C','B','A'},
  {'#','9','6','3'},
  {'0','8','5','2'},
  {'*','7','4','1'}
};

byte alarma_on[8] = {
  B00000,
  B00100,
  B01110,
  B01110,
  B11111,
  B01110,
  B00000,
};

byte alarma_off[8] = {
  B00001,
  B00101,
  B01110,
  B01110,
  B11111,
  B11110,
  B10000,
};


#define TECLADO_PIN1 31
#define TECLADO_PIN2 33
#define TECLADO_PIN3 35
#define TECLADO_PIN4 37

#define TECLADO_PIN5 43
#define TECLADO_PIN6 45
#define TECLADO_PIN7 47
#define TECLADO_PINA 49

byte colPins[COLS] = {TECLADO_PINA, TECLADO_PIN7, TECLADO_PIN6, TECLADO_PIN5}; // TECLADO_PINA = A del teclado
byte rowPins[ROWS] = {TECLADO_PIN4, TECLADO_PIN3, TECLADO_PIN2, TECLADO_PIN1}; // TECLADO_PIN1 = 1 del teclado


//Variables globales
float temperatura = 0.0f;
float humedad = 0.0f;
bool enModoLecturaNuevaIP = false;
int ipIngresadaPorUsuario[4];
int posicionIpIngreso = 0;
char bufferIp[3];
int posicionBufferIp = 0;
bool ocurrioError = false;
bool esErrorGrave = false;
String motivoError;
unsigned long tiempoInicioError;

//buzz
bool hacerSonarBuzz = false;
unsigned long tiempoUltimoBuzz = 0;
unsigned long tiempoPrimerBuzz = 0;
int cantidadVecesSonoBuzz = 0;
bool fueLeidoErrorPorAlarma = true;

// Movimiento + alarma
int movimiento = 0;
bool modoAlarmaActivo = false;

//tamanio del buffer para los json
#define BUFFER_JSON 1024
#define TIEMPO_HASTA_ERROR_PERMANENTE 10000

void setup() {
  Serial.begin(9600);
  while (!Serial) continue;
  
  inicializarModuloServer();
  inicializarDisplay();
  inicializarModuloTemperatura();
  inicializarSensorMovimiento();
  inicializarTeclado();
  inicializarBuzzer();
}



void loop() {

  delay(200);  

  lecturaMovimientoYBuzzer();
  lecturaDeTemperatura();
  procesarInputTeclado();
  procesarRequestDelServidor();
  revisarErrores();

}

void procesarRequestDelServidor(){

  EthernetClient client = server.available();
  if (!client) {
    return;    
  }

  Serial.println(F("New client"));

  while (client.available()) client.read();
  StaticJsonDocument<JSON_ARRAY_SIZE(100)> docArray;  
  JsonArray array = docArray.to<JsonArray>();
  if(ocurrioError){
    array.add(motivoError);    
  }
  if(hacerSonarBuzz || !fueLeidoErrorPorAlarma){
    fueLeidoErrorPorAlarma = true;
    array.add(String("Hubo movimiento con alarma on!")); 
  }

  StaticJsonDocument<BUFFER_JSON> doc;
  doc["temperatura"] = temperatura;
  doc["humedad"] = humedad;
  doc["movimiento"] = movimiento == 1 ? true : false;
  doc["errores"] = array;
  

  enviarJsonAlCliente(doc, client);

  client.stop();
  
}

void enviarJsonAlCliente(StaticJsonDocument<BUFFER_JSON> doc, EthernetClient client){

  Serial.print(F("Sending: "));
  serializeJson(doc, Serial);
  Serial.println();

  client.println(F("HTTP/1.0 200 OK"));
  client.println(F("Content-Type: application/json"));
  client.println(F("Connection: close"));
  client.print(F("Content-Length: "));
  client.println(measureJsonPretty(doc));
  client.println();

  // Write JSON document
  serializeJsonPretty(doc, client);
  
}

void lecturaDeTemperatura(){

  humedad = dht.readHumidity();
  float temperaturaSinProcesar = dht.readTemperature();

  if (isnan(humedad) || isnan(temperaturaSinProcesar)) {
    Serial.println("Error obteniendo los datos del sensor DHT11");
    if(!ocurrioError){
      tiempoInicioError = millis();
      ocurrioError = true; 
      motivoError = String("error con temperatura");         
    }
    lcd.noBlink();
    lcd.setCursor(0,1);
    if(esErrorGrave){
      lcd.print("ERR:temperatura ");
    } else {
      lcd.print("WRN:temperatura ");      
    }
    return;
  } else {
    ocurrioError = false;
    esErrorGrave = false;
  }

  temperatura = dht.computeHeatIndex(temperaturaSinProcesar, humedad, false);

  char bufferResultadoT[8];
  dtostrf(temperatura,4,2,bufferResultadoT);
  char bufferResultadoH[8];
  int humedadTruncada = humedad;

  lcd.noBlink();
  lcd.setCursor(3,1);
  lcd.print(" T:");
  lcd.print(bufferResultadoT);
  lcd.print(" H:");
  lcd.print(humedadTruncada);
}

void procesarInputTeclado(){

  char caracterLeido = lecturaDeTeclado();

  if(caracterLeido == 'A'){
      Serial.println(F("Se entra en modo cambio de IP"));
      enModoLecturaNuevaIP = true;
      //lcd.setCursor(0,0);      
  }

  if(caracterLeido == 'C'){

      if(modoAlarmaActivo){
        modoAlarmaActivo = false;
      } else {
        modoAlarmaActivo = true;
      }
  }

  if(enModoLecturaNuevaIP){
    lcd.blink();
    lcd.setCursor(posicionIpIngreso * 4 + posicionBufferIp , 0);
    if(isDigit(caracterLeido)){
      bufferIp[posicionBufferIp] = caracterLeido;
      posicionBufferIp++;
      lcd.write(caracterLeido);
      if(posicionBufferIp == 3){
        ipIngresadaPorUsuario[posicionIpIngreso] = atoi(bufferIp);
        posicionIpIngreso++;
        posicionBufferIp = 0;
        if(posicionIpIngreso < 4){
          lcd.write('.');
        } else {
          Serial.println("se settea la IP");
          IPAddress nuevaIp(ipIngresadaPorUsuario[0], ipIngresadaPorUsuario[1], ipIngresadaPorUsuario[2], ipIngresadaPorUsuario[3]);
          
          Ethernet.begin(mac, nuevaIp, myDns, gateway, subnet);
          server.begin();
    
          Serial.println(F("Server is ready again."));
          Serial.println(Ethernet.localIP());
          posicionIpIngreso = 0;
          enModoLecturaNuevaIP = false;
          posicionBufferIp = 0;
          //lcd.noBlink();  
        }
      }
    } else if(caracterLeido == 'B' && posicionBufferIp > 0) {
      posicionBufferIp--;
    } else if(caracterLeido == 'B' && posicionBufferIp == 0 && posicionIpIngreso > 0){
      posicionIpIngreso--;
    }
  }
  
}

void inicializarModuloTemperatura(){
  dht.begin();  
}


void inicializarSensorMovimiento(){
  pinMode(MOVIMIENTO_PIN_IN, INPUT);
}

void lecturaMovimientoYBuzzer(){
 movimiento = digitalRead(MOVIMIENTO_PIN_IN);

  Serial.println(movimiento);

 if(movimiento == HIGH && modoAlarmaActivo && !hacerSonarBuzz) {
  fueLeidoErrorPorAlarma = false;
  hacerSonarBuzz = true;
  tiempoUltimoBuzz = millis();
  tiempoPrimerBuzz = millis();
 }

 if(hacerSonarBuzz && millis() > tiempoUltimoBuzz + DELAY_BUZZ_EN_MILLI){
  tiempoUltimoBuzz = millis();
  
  digitalWrite(BUZZER_PIN,HIGH);
  delay(580);
  digitalWrite(BUZZER_PIN,LOW);
 }

 if(hacerSonarBuzz && (millis() > (tiempoPrimerBuzz + TIEMPO_ALARMA_BUZZ))){
  hacerSonarBuzz = false;  
 }

  lcd.noBlink();
  lcd.setCursor(0,1);
  lcd.write(byte(1));    
  if(modoAlarmaActivo == true){
    lcd.print("SI");
  }else{
    lcd.print("NO");
  }
}

void inicializarTeclado(){

  pinMode(TECLADO_PINA, OUTPUT);
  pinMode(TECLADO_PIN7, OUTPUT);
  pinMode(TECLADO_PIN6, OUTPUT);
  pinMode(TECLADO_PIN5, OUTPUT);
  pinMode(TECLADO_PIN4, INPUT_PULLUP);
  pinMode(TECLADO_PIN3, INPUT_PULLUP);
  pinMode(TECLADO_PIN2, INPUT_PULLUP);
  pinMode(TECLADO_PIN1, INPUT_PULLUP);
  
}

char lecturaDeTeclado(){

  char caracterLeido = ' ';
  for(int i = 0; i < ROWS; i++){
      digitalWrite(TECLADO_PINA - 2*i, LOW);    
      for(int j = 0; j < COLS; j++){
        int valorLeido = digitalRead(TECLADO_PIN4 - 2*j);
        delay(4);
        if(valorLeido == LOW ){
          caracterLeido = keys[i][j];        
          Serial.println(keys[i][j]);
        }
      }
      digitalWrite(TECLADO_PINA - 2*i, HIGH);    
  }

  return caracterLeido;
}

void inicializarModuloServer(){

  Ethernet.begin(mac, ip, myDns, gateway, subnet);
  server.begin();

  Serial.println(F("Server is ready."));
  Serial.println(Ethernet.localIP());
  
}

void inicializarBuzzer(){

  pinMode(BUZZER_PIN, OUTPUT);
   
}

void revisarErrores(){

  if( millis() - tiempoInicioError > TIEMPO_HASTA_ERROR_PERMANENTE && ocurrioError){
    esErrorGrave = true;
  } else {
    esErrorGrave = false;
  }
  
}

void inicializarDisplay(){

  lcd.begin(16, 2);
  lcd.print("192.168.002.213");

  lcd.createChar(0, alarma_off);
  lcd.createChar(1, alarma_on);
  
}
