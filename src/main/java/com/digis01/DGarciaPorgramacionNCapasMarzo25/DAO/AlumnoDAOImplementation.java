package com.digis01.DGarciaPorgramacionNCapasMarzo25.DAO;


import com.digis01.DGarciaPorgramacionNCapasMarzo25.JPA.AlumnoDireccion;
import com.digis01.DGarciaPorgramacionNCapasMarzo25.JPA.Result;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository // logica de base de datos
public class AlumnoDAOImplementation implements IAlumnoDAO {

    @Autowired // conexión de JPA
    private EntityManager entityManager;

  
    @Override
    public Result GetAllJPA() {
        //  Esto es lenguaje JPQL
        Result result = new Result();
        try {

            TypedQuery<com.digis01.DGarciaPorgramacionNCapasMarzo25.JPA.Alumno> queryAlumnos = entityManager.createQuery("FROM Alumno", com.digis01.DGarciaPorgramacionNCapasMarzo25.JPA.Alumno.class);
            List<com.digis01.DGarciaPorgramacionNCapasMarzo25.JPA.Alumno> alumnos = queryAlumnos.getResultList();
            result.objects = new ArrayList<>();
            for (com.digis01.DGarciaPorgramacionNCapasMarzo25.JPA.Alumno alumnoJPA : alumnos) {

                AlumnoDireccion alumnoDireccion = new AlumnoDireccion();
                alumnoDireccion.Alumno = alumnoJPA;
                TypedQuery<com.digis01.DGarciaPorgramacionNCapasMarzo25.JPA.Direccion> queryDireccion = entityManager.createQuery("FROM Direccion WHERE Alumno.IdAlumno = :idalumno", com.digis01.DGarciaPorgramacionNCapasMarzo25.JPA.Direccion.class);
                queryDireccion.setParameter("idalumno", alumnoJPA.getIdAlumno());

                List<com.digis01.DGarciaPorgramacionNCapasMarzo25.JPA.Direccion> direccionesJPA = queryDireccion.getResultList();
                alumnoDireccion.Direcciones = direccionesJPA;

                result.objects.add(alumnoDireccion);

            }

            result.correct = true;
        } catch (Exception ex) {
            result.correct = false;
            result.errorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }

        return result;

    }

    

}
