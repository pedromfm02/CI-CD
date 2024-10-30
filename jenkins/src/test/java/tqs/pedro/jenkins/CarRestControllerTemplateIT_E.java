package tqs.pedro.jenkins;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import tqs.pedro.jenkins.Entity.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)


//@AutoConfigureTestDatabase

// switch AutoConfigureTestDatabase with TestPropertySource to use a real database
@TestPropertySource( locations = "application-integrationtest.properties")

public class CarRestControllerTemplateIT_E {
    
    // will need to use the server port for the invocation url
    @LocalServerPort
    int randomServerPort;

    // a REST client that is test-friendly
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CarRepository repository;

    @AfterEach
    public void resetDb() {
        repository.deleteAll();
    }

    @Test
    public void whenValidInput_ThenCreateCar(){
        Car car1 = new Car("Audi","A3");
        ResponseEntity<Car> entity = restTemplate.postForEntity("/api/Car", car1, Car.class);

        List<Car> found = repository.findAll();
        assertThat(found).extracting(Car::getMaker).containsOnly(car1.getMaker());
    }

    @Test
    public void givenCars_whenGetCarByValidId_ThenReturnCar(){
        Car car1 = new Car("Audi","A3");
        repository.saveAndFlush(car1);

        ResponseEntity<Car> response = restTemplate.exchange("/api/Cars/1", HttpMethod.GET, null, Car.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        
        List<Car> cars = repository.findAll();
        assertThat(cars).extracting(Car::getMaker).containsOnly(car1.getMaker());
    }
    
    @Test
    public void givenCars_whenGetCarByInvalidId_ThenException(){
        ResponseEntity<Car> response = restTemplate.exchange("/api/Cars/1", HttpMethod.GET, null, Car.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        
        List<Car> cars = repository.findAll();
        assertThat(cars).extracting(Car::getMaker).isEmpty();
    }
    
    @Test
    public void givenCars_whenGetCar_ThenReturnCars(){
        Car car1 = new Car("Audi","A3");
        Car car2 = new Car("Opel","Corsa");
        repository.save(car1);
        repository.save(car2);
        
        ResponseEntity<List<Car>> response = restTemplate
        .exchange("/api/Cars", HttpMethod.GET, null, new ParameterizedTypeReference<List<Car>>() {
        });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).extracting(Car::getMaker).containsExactly(car1.getMaker(), car2.getMaker());

    }

}
