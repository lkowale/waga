void loop() {
   
if(stanPracy==NORMAL)
  loop_normal();
  
if(stanPracy==TAROWANIE)
  tarowanie(); 

//if(stanPracy==KALIBRACJA)
//  kalibracja();

 
  
//jesli cos przyszlo przetworz to
if(recive_bluetooth())
    wprowadzanie();
    
  ilosc_petli++;
}
