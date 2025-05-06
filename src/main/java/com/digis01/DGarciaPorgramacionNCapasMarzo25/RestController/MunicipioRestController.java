
package com.digis01.DGarciaPorgramacionNCapasMarzo25.RestController;

import com.digis01.DGarciaPorgramacionNCapasMarzo25.DAO.MunicipioDAOImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/municipioapi")
public class MunicipioRestController {
    
    
    @Autowired
    private MunicipioDAOImplementation municipioDAOImplementation;
    
    @GetMapping("byidestado/{idEstado}")
    public ResponseEntity GetByIdEstado(@PathVariable int idEstado){
        return ResponseEntity.ok(municipioDAOImplementation.GetByEstado(idEstado));
    }
}
