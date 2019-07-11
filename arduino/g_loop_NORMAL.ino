void loop_normal()
{ 

   // pobierz dane z wagi
   if (poczatek_okna_waga + OKNO_WAGA < millis()){
         waga=scale.get_units(4);
         poczatek_okna_waga=millis();
      //wariancja odczytu wagi
         wagaT[indeksT++]=scale.get_value();
         indeksT=indeksT%ROZMIART;
         wagaR=0;           
          for(int i=0;i<ROZMIART;i++)
          {
            wagaR+=abs(wagaT[i]);
//           Serial.print(" wagaR");         
//           Serial.print(wagaR);             
          }
         wagaR=(float)wagaR/(float)ROZMIART;  
//           Serial.print(" wagaRfinal:");         
//           Serial.println(wagaR); 
         
                     
        rootSend["c"] = "i";
        rootSend["1"] = waga;
        rootSend["2"] = freeMemory();    
        rootSend["3"] = ilosc_petli; 
        rootSend["4"] = wagaR;         
                
        rootSend.printTo(mySerial);
        mySerial.print("#"); 
//        rootSend.printTo(Serial);           
         ilosc_petli=0;      
   }
          
   
}

