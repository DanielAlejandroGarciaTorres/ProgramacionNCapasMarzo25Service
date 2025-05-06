package com.digis01.DGarciaPorgramacionNCapasMarzo25.RestController;

import com.digis01.DGarciaPorgramacionNCapasMarzo25.DAO.SemestreDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/semestreapi")
public class SemestreRestController {
    
    @Autowired
    private SemestreDAO semestreDAO;
    
    @GetMapping
    public ResponseEntity GetAll(){
        
        return ResponseEntity.ok(semestreDAO.GetAll());
    }
}
