package com.digis01.DGarciaPorgramacionNCapasMarzo25.DAO;

import com.digis01.DGarciaPorgramacionNCapasMarzo25.JPA.AlumnoDireccion;
import com.digis01.DGarciaPorgramacionNCapasMarzo25.JPA.Result;


public interface IAlumnoDAO {
 
   
    Result GetAllJPA();
    
    Result AddJPA(AlumnoDireccion alumnoDireccion);
    
}
