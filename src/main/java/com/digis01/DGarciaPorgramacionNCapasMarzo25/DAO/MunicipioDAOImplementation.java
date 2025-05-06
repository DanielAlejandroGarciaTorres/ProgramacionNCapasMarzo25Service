/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.digis01.DGarciaPorgramacionNCapasMarzo25.DAO;

import com.digis01.DGarciaPorgramacionNCapasMarzo25.JPA.Municipio;
import com.digis01.DGarciaPorgramacionNCapasMarzo25.JPA.Result;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MunicipioDAOImplementation  implements IMunicipioDAO{

    @Autowired
    private EntityManager entityManager;
    
    @Override
    public Result GetByEstado(int idEstado) {
        
        Result result = new Result();
        
        try {
            TypedQuery<Municipio> queryMunicipio = entityManager.createQuery("FROM Municipio WHERE Estado.IdEstado = :idEstado", Municipio.class);
            queryMunicipio.setParameter("idEstado", idEstado);
            result.object = queryMunicipio.getResultList();
            
        } catch (Exception ex){
            
        }
        
        return result;
    }
    
}
