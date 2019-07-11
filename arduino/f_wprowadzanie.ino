/////////////////////////////////////                                          wprowadzanie      ///////////////////////  ///////////////////////  ///////////////////////  ///////////////////////  ///////////////////////

void wprowadzanie()
{
  //tarowanie
  if(commandString.startsWith("t1",0))
  {
//   Serial.println("t1");    
      stanPracy=TAROWANIE;
//      commandString.clear();
  }
  
  //kalibracja poczatek
  if(commandString.startsWith("k1",0))
  {
//   Serial.println("k1");    
      stanPracy=SKALOWANIE;

      String waga_s=commandString.substring(2);
      int waga=waga_s.toInt();



      
//          float odczyt10 = scale.get_units(10);
          float odczyt10 = scale.get_value(10);
          storage.scaleGain = (float)odczyt10  / (float)waga;
          scale.set_scale(storage.scaleGain);

Serial.print("odczyt10:"); Serial.print(odczyt10); 
Serial.print(" waga:"); Serial.print(waga); 
Serial.print(" storage.scaleGain:"); Serial.println(storage.scaleGain); 

        
        
          saveConfig();
        rootSendCommand["c"] = "k5";           
        rootSendCommand["1"] = storage.scaleGain;      
        rootSendCommand.printTo(mySerial);
        mySerial.print("#"); 
        stanPracy=NORMAL;         
  }
  
  //kalibracja polozono ciezar
  if(commandString.startsWith("k4",0))
  {
          float odczyt10 = scale.get_units(10);
          storage.scaleGain = (float)odczyt10  / (float)storage.wagaSkalowania;
          scale.set_scale(storage.scaleGain);
        
          saveConfig();
        rootSendCommand["c"] = "k5";           
        rootSendCommand["1"] = storage.scaleGain;      
        rootSendCommand.printTo(mySerial);
        mySerial.print("#"); 
          
  }
  
  if(commandString.startsWith("k6",0))
  {
//   Serial.println("k6");

          scale.tare();

          storage.offset=scale.get_offset();         
          saveConfig();
        rootSendCommand["c"] = "k7";           
        rootSendCommand["v"] = storage.offset;      
        rootSendCommand.printTo(mySerial);
        mySerial.print("#"); 
         
        stanPracy=NORMAL;
  }  
  

 
  if (commandString.startsWith("go", 0))
  {     
        stanPracy=NORMAL;
        Serial.println("Start"); 
        delay(OKNO_WAGA);
  }     
  if (commandString.startsWith("stop", 0))
  {
        stanPracy=STOP;
        Serial.println("Stop"); 
  }    
}
