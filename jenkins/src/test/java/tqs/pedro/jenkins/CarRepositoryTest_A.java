package tqs.pedro.jenkins;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import tqs.pedro.jenkins.Entity.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * DataJpaTest limits the test scope to the data access context (no web environment loaded, for example)
 * tries to autoconfigure the database, if possible (e.g.: in memory db)
 */
@DataJpaTest
class CarRepositoryTest_A{
    
    // get a test-friendly Entity Manager
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CarRepository carRepository;

    @Test
    public void whenfindCarById_thenReturnCar(){
        // arrange a new car and insert into db
        Car car1 = new Car("Audi", "A3");
        entityManager.persistAndFlush(car1); //ensure data is persisted at this point

        // test the query method of interest
        Car found = carRepository.findByCarId(car1.getCarId());
        assertThat(found).isEqualTo(car1);
    }
    
    @Test
    public void whenInvalidCarId_thenReturnNull(){
        Car found = carRepository.findByCarId(-1L);
        assertThat(found).isNull();
    }

    
    @Test
    public void given3Cars_whenFindAll_thenReturnAllCars(){
        Car car1 = new Car("Audi", "A3");
        Car car2 = new Car("Ford", "Punto");
        Car car3 = new Car("Opel", "Corsa");

        entityManager.persist(car1);
        entityManager.persist(car2);
        entityManager.persist(car3);
        entityManager.flush();

        List<Car> allCars = carRepository.findAll();

        assertThat(allCars).hasSize(3).extracting(Car::getMaker).containsOnly(car1.getMaker(),car2.getMaker(),car3.getMaker());
    }

}