void setup() {

    setup_bluetooth(); 
  
    loadConfig();
//    if(storage.mapSet) loadMap();
      
  scale.set_scale(storage.scaleGain);
  scale.set_offset(storage.offset); 
  poczatek_okna_kolo=millis();  
  poczatek_okna_waga=millis();
  czas_walek_wprowadzajacy=millis();
  czas_walek=millis();  

    pinMode(walekWprowadzajacy, OUTPUT); 
    digitalWrite(walekWprowadzajacy, LOW);    
  
  Serial.begin(9600);   

  attachPinChangeInterrupt(PIN_KOLO, przerwanie_kolo, RISING);  
  attachPinChangeInterrupt(PIN_WALEK, przerwanie_walek, RISING);    
  attachPinChangeInterrupt(PIN_WPROWADZAJACY, przerwanie_walek_wprowadzajacy, RISING);   
  
  delay(100);   
}


