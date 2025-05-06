package com.digis01.DGarciaPorgramacionNCapasMarzo25.DAO;

import com.digis01.DGarciaPorgramacionNCapasMarzo25.JPA.Result;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SemestreDAO implements ISemestreDAO{

    @Autowired
    private EntityManager entityManager;
    
    @Override
    public Result GetAll() {
        Result result = new Result();
        
        try {
            
            TypedQuery<com.digis01.DGarciaPorgramacionNCapasMarzo25.JPA.Semestre> querySemestre = entityManager.createQuery("FROM Semestre", com.digis01.DGarciaPorgramacionNCapasMarzo25.JPA.Semestre.class);
            result.object = querySemestre.getResultList();
            result.correct = true;
            
        }catch (Exception ex) {
            
        }
        
        return result;
    }
    
}
