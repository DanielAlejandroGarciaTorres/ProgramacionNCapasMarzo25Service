package com.digis01.DGarciaPorgramacionNCapasMarzo25.RestController;

import com.digis01.DGarciaPorgramacionNCapasMarzo25.DAO.AlumnoDAOImplementation;
import com.digis01.DGarciaPorgramacionNCapasMarzo25.JPA.Alumno;
import com.digis01.DGarciaPorgramacionNCapasMarzo25.JPA.AlumnoDireccion;
import com.digis01.DGarciaPorgramacionNCapasMarzo25.JPA.Colonia;
import com.digis01.DGarciaPorgramacionNCapasMarzo25.JPA.Direccion;
import com.digis01.DGarciaPorgramacionNCapasMarzo25.JPA.Result;
import com.digis01.DGarciaPorgramacionNCapasMarzo25.JPA.ResultFile;
import com.digis01.DGarciaPorgramacionNCapasMarzo25.JPA.Semestre;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/alumnoapi")
public class AlumnoRestController {

    @Autowired
    private AlumnoDAOImplementation alumnoDAOImplementation;

    @GetMapping
    public ResponseEntity GetAll() {
        Result result = alumnoDAOImplementation.GetAllJPA();
        if (result.correct) {
            if (result.objects.isEmpty()) {
                return ResponseEntity.status(204).body(null);
            } else {
                return ResponseEntity.ok(result);
            }
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    @PostMapping("/Add")
    public ResponseEntity Add(@RequestBody AlumnoDireccion alumnoDireccion) {

        Result result = alumnoDAOImplementation.AddJPA(alumnoDireccion);

        if (result.correct) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().build();
        }

    }

    @DeleteMapping("/delete/{IdAlumno}")
    public ResponseEntity AlumnoDelete(@PathVariable int IdAlumno) {

        Result result = alumnoDAOImplementation.AlumnoDelete(IdAlumno);

        if (result.correct) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/CargaMasiva")
    public ResponseEntity CargaMasiva(@RequestParam("archivo") MultipartFile archivo) {
        Result result = new Result();
        if (!archivo.isEmpty() || archivo != null) {

            try {
                String tipoArchivo = archivo.getOriginalFilename().split("\\.")[1];

                String root = System.getProperty("user.dir"); //Obtener direccion del proyecto en el equipo
                String path = "src/main/resources/static/archivos"; //Path relativo dentro del proyecto
                String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmSS"));
                String absolutePath = root + "/" + path + "/" + fecha + archivo.getOriginalFilename();
                archivo.transferTo(new File(absolutePath));
                //Leer el archivo
                List<AlumnoDireccion> listaAlumnos = new ArrayList();
                if (tipoArchivo.equals("txt")) {
                    listaAlumnos = LecturaArchivoTXT(new File(absolutePath)); //método para leer la lista
                } else {
                    listaAlumnos = LecturaArchivoExcel(new File(absolutePath));
                }

                //Validar el archivo
                List<ResultFile> listaErrores = new ArrayList<>();//ValidarArchivo(listaAlumnos);

                if (listaErrores.isEmpty()) {
                    //Proceso mi archivo
                    result.correct = true;
                    result.object = absolutePath;
                    return ResponseEntity.ok(result); //ResultFile o Result?
                } else {
                    result.correct = false;
                    result.objects = new ArrayList();

                    for (ResultFile error : listaErrores) {
                        result.objects.add(error);
                    }

                    return ResponseEntity.status(400).body(result);
                }

            } catch (Exception ex) {
                return ResponseEntity.status(500).body("Todo mal");
            }

        } else {
            result.correct = false;
            return ResponseEntity.status(400).body(result);
        }

    }

    @PostMapping("/CargaMasiva/Procesar")
    public ResponseEntity Procesar(@RequestBody String absolutePath) {
        Result result = new Result();

        try {
            String tipoArchivo = absolutePath.split("\\.")[1];

            List<AlumnoDireccion> listaAlumnos = new ArrayList();
            if (tipoArchivo.equals("txt")) {
                listaAlumnos = LecturaArchivoTXT(new File(absolutePath)); //método para leer la lista
            } else {
                listaAlumnos = LecturaArchivoExcel(new File(absolutePath));
            }

            for (AlumnoDireccion alumno : listaAlumnos) {
                alumnoDAOImplementation.AddJPA(alumno);
            }

            result.correct = true;

        } catch (Exception ex) {
            result.correct = false;
        }

        return ResponseEntity.ok(result);
    }

    public List<AlumnoDireccion> LecturaArchivoTXT(File archivo) {
        List<AlumnoDireccion> listaAlumnos = new ArrayList<>();

        try (FileReader fileReader = new FileReader(archivo); BufferedReader bufferedReader = new BufferedReader(fileReader);) {

            String linea;

            while ((linea = bufferedReader.readLine()) != null) {
                String[] campos = linea.split("\\|");

                AlumnoDireccion alumnoDireccion = new AlumnoDireccion();
                alumnoDireccion.Alumno = new Alumno();
                alumnoDireccion.Alumno.setNombre(campos[0]);
                alumnoDireccion.Alumno.setApellidoPaterno(campos[1]);
                alumnoDireccion.Alumno.setApellidoMaterno(campos[2]);
                alumnoDireccion.Alumno.setUsername(campos[3]);
                alumnoDireccion.Alumno.setEmail(campos[4]);
                //Darle formato a la fecha de nacimiento
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // Dar formato a la fecha
                alumnoDireccion.Alumno.setFechaNacimiento(formatter.parse(campos[5]));
                //alumnoDireccion.Alumno.setStatus(Integer.parseInt(campos[6]));
                alumnoDireccion.Alumno.setImagen(null);
                alumnoDireccion.Alumno.Semestre = new Semestre();
                alumnoDireccion.Alumno.Semestre.setIdSemestre(Integer.parseInt(campos[7]));

                alumnoDireccion.Direccion = new Direccion();
                alumnoDireccion.Direccion.setCalle(campos[8]);
                alumnoDireccion.Direccion.setNumeroExterior(campos[9]);
                alumnoDireccion.Direccion.setNumeroInterior(campos[10]);

                alumnoDireccion.Direccion.Colonia = new Colonia();
                alumnoDireccion.Direccion.Colonia.setIdColonia(Integer.parseInt(campos[11]));

                listaAlumnos.add(alumnoDireccion);
            }

        } catch (Exception ex) {
            listaAlumnos = null;
        }

        return listaAlumnos;
    }

    public List<AlumnoDireccion> LecturaArchivoExcel(File archivo) {
        List<AlumnoDireccion> listaAlumnos = new ArrayList<>();
        try (XSSFWorkbook workbook = new XSSFWorkbook(archivo);) {
            for (Sheet sheet : workbook) {

                for (Row row : sheet) {

                    AlumnoDireccion alumnoDireccion = new AlumnoDireccion();
                    alumnoDireccion.Alumno = new Alumno();
                    alumnoDireccion.Alumno.setNombre(row.getCell(0).toString());
                    alumnoDireccion.Alumno.setApellidoPaterno(row.getCell(1).toString());
                    alumnoDireccion.Alumno.setApellidoMaterno(row.getCell(2).toString());
                    alumnoDireccion.Alumno.setEmail(row.getCell(3).toString());
                    //alumnoDireccion.Alumno.Semestre = new Semestre();
                    //alumnoDireccion.Alumno.Semestre.setIdSemestre(Integer.parseInt(row.getCell(4).toString()));
                    //alumnoDireccion.Alumno.setStatus(row.getCell(3) != null ? (int) row.getCell(3).getNumericCellValue() : 0 );
                    listaAlumnos.add(alumnoDireccion);
                }

            }
        } catch (Exception ex) {
            System.out.println("Error al abrir el archivo");
        }

        return listaAlumnos;
    }

    public List<ResultFile> ValidarArchivo(List<AlumnoDireccion> listaAlumnos) {
        List<ResultFile> listaErrores = new ArrayList<>();

        if (listaAlumnos == null) {
            listaErrores.add(new ResultFile(0, "La lista es nula", "La lista es nula"));
        } else if (listaAlumnos.isEmpty()) {
            listaErrores.add(new ResultFile(0, "La lista está vacía", "La lista está vacía"));
        } else {
            int fila = 1;
            for (AlumnoDireccion alumnoDireccion : listaAlumnos) {
                if (alumnoDireccion.Alumno.getNombre() == null || alumnoDireccion.Alumno.getNombre().equals("")) {
                    listaErrores.add(new ResultFile(fila, alumnoDireccion.Alumno.getNombre(), "El nombre es un campo oligatorio"));
                }

                if (alumnoDireccion.Alumno.getApellidoPaterno() == null || alumnoDireccion.Alumno.getApellidoPaterno().equals("")) {
                    listaErrores.add(new ResultFile(fila, alumnoDireccion.Alumno.getApellidoPaterno(), "El Apellido Paterno es un campo oligatorio"));
                }

                if (alumnoDireccion.Alumno.getUsername() == null || alumnoDireccion.Alumno.getUsername().equals("")) {
                    listaErrores.add(new ResultFile(fila, alumnoDireccion.Alumno.getApellidoPaterno(), "El Username es un campo oligatorio"));
                }
                fila++;
            }
        }
        return listaErrores;
    }

}
