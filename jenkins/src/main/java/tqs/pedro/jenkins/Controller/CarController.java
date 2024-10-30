package tqs.pedro.jenkins.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tqs.pedro.jenkins.Entity.Car;
import tqs.pedro.jenkins.Service.CarManagerService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api")
public class CarController {
    
    private final CarManagerService Service;

    public CarController(CarManagerService service){
        this.Service = service;
    }

    @PostMapping("/Car")
    public ResponseEntity<Car> createCar(@RequestBody Car car) {
        HttpStatus status = HttpStatus.CREATED;
        Car saved = Service.save(car);
        return new ResponseEntity<Car>(saved, status);
    }

    @GetMapping("/Cars")
    public ResponseEntity<List<Car>> getAllCars() {
        HttpStatus status = HttpStatus.OK;
        List<Car> carList = Service.getAllCars();
        return new ResponseEntity<List<Car>>(carList,status);
    }

    @GetMapping("/Cars/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable("id") Long id) {
        HttpStatus status;
        Car car = Service.getCarDetails(id);
        if (car==null){
            status = HttpStatus.NOT_FOUND;
        }else{
            status = HttpStatus.OK;
        }
        return new ResponseEntity<Car>(car,status);
    }
    
}
