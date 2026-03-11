package gm.rh.controlador;

import gm.rh.exepcion.RecursoNoEncontradoExepcion;
import gm.rh.modelo.Empleado;
import gm.rh.servicio.EmpleadoServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
//http://localhost:8080/rh-app/
@RequestMapping("rh-app")
@CrossOrigin(value = "http://localhost:3000")//es para enviar peticiones REACT el Front-End
public class empleadoControlador {
    //es para mandar informacion a consola
    private static final Logger logger =
            LoggerFactory.getLogger(empleadoControlador.class);
    //lo podemos injectar por que ya esta en la fabrica de spring
    @Autowired
    private EmpleadoServicio empleadoServicio;

    @GetMapping("/empleados")
    public List<Empleado> obtenerEmpleados(){
        var empleados = empleadoServicio.listarEmpleado();
        empleados.forEach((empleado -> logger.info(empleado.toString())));
        return empleados;
    }

    @PostMapping("/empleados")
    public Empleado agregarEmpleado(@RequestBody Empleado empleado){
        logger.info("Empleado a agregar:"+empleado);
        return empleadoServicio.guardarEmpleado(empleado);

    }
    @GetMapping("/empleados/{id}")
    public ResponseEntity<Empleado>
    obtenerEmpleadoPorId(@PathVariable Integer id){
        Empleado empleado = empleadoServicio.buscarEmpleadoPorId(id);
    if (empleado==null)
        throw new RecursoNoEncontradoExepcion("NO se encontro "+empleado);
    return ResponseEntity.ok(empleado);
    }
    @PutMapping("/empleados/{id}")
    public ResponseEntity<Empleado>
    actualizarEmpleado(@PathVariable Integer id,
                       @RequestBody Empleado empleadoRecibido){
        Empleado empleado = empleadoServicio.buscarEmpleadoPorId(id);
        if(empleado==null)
            throw new RecursoNoEncontradoExepcion("el id recibido no existe:"+id);
        empleado.setNombre(empleadoRecibido.getNombre());
        empleado.setDepartamento(empleadoRecibido.getDepartamento());
        empleado.setSueldo(empleadoRecibido.getSueldo());
        empleadoServicio.guardarEmpleado(empleado);
        return ResponseEntity.ok(empleado);
    }
    @DeleteMapping("/empleados/{id}")
    public ResponseEntity<Map<String,Boolean>>
    eliminarEmpleado(@PathVariable Integer id){
     Empleado empleado= empleadoServicio.buscarEmpleadoPorId(id);
     if(empleado==null)
         throw new RecursoNoEncontradoExepcion("El id recibido no encontrado:"+id);
     empleadoServicio.eliminarEmpleado(empleado);
     //json{eliminado:true]
        Map<String,Boolean>respuesta=new HashMap<>();
        respuesta.put("eliminado",Boolean.TRUE);
        return ResponseEntity.ok(respuesta);
    }

}
