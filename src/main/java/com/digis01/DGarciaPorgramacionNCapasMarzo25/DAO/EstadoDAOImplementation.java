/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.digis01.DGarciaPorgramacionNCapasMarzo25.DAO;

import com.digis01.DGarciaPorgramacionNCapasMarzo25.JPA.Estado;
import com.digis01.DGarciaPorgramacionNCapasMarzo25.JPA.Result;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EstadoDAOImplementation implements IEstadoDAO{
    
    @Autowired
    private EntityManager entityManager;

    @Override
    public Result GetAll() {
        
        Result result = new Result();
        
        try {
            
            TypedQuery<Estado> queryEstado = entityManager.createQuery("FROM Estado", Estado.class);
            result.object = queryEstado.getResultList();
            result.correct = true;
            
        } catch (Exception ex){
            
        }
        
        return result;
        
    }
    
}
