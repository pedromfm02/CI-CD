package tqs.pedro.jenkins.Service;

import java.util.List;
import org.springframework.stereotype.Service;

import tqs.pedro.jenkins.Entity.Car;
import tqs.pedro.jenkins.Entity.CarRepository;

@Service
public class CarManagerServiceImpl implements CarManagerService{
    
    private final CarRepository repo;

    public CarManagerServiceImpl(CarRepository repository){
        this.repo = repository;
    }

    @Override
    public Car getCarDetails(Long id){
        return repo.findByCarId(id);
    }

    @Override
    public List<Car> getAllCars(){
        return repo.findAll();
    }

    @Override
    public Car save(Car car){
        return repo.save(car);
    }
}
