package com.digis01.DGarciaPorgramacionNCapasMarzo25.RestController;

import com.digis01.DGarciaPorgramacionNCapasMarzo25.DAO.EstadoDAOImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/estadoapi")
public class EstadoRestController {
    
    @Autowired
    private EstadoDAOImplementation  estadoDAOImplementation;
    
    @GetMapping
    public ResponseEntity GetAll(){
        return ResponseEntity.ok(estadoDAOImplementation.GetAll());
    }
    
}
