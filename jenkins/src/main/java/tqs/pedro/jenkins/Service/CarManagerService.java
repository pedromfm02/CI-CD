package tqs.pedro.jenkins.Service;

import java.util.List;

import tqs.pedro.jenkins.Entity.Car;

public interface CarManagerService {
    
    public Car save(Car car);

    public List<Car> getAllCars();

    public Car getCarDetails(Long id);
} 
