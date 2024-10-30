package tqs.pedro.jenkins;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import tqs.pedro.jenkins.Entity.Car;
import tqs.pedro.jenkins.Entity.CarRepository;
import tqs.pedro.jenkins.Service.CarManagerService;
import tqs.pedro.jenkins.Service.CarManagerServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test scenario: verify the logic of the Service, mocking the response of the datasource
 * Results in standard unit test with mocks
 */
@ExtendWith(MockitoExtension.class)
public class CarService_UnitTest_B {
    @Mock(lenient = true)
    private CarRepository carRepository;

    @InjectMocks
    private CarManagerServiceImpl carService;

    @BeforeEach
    public void setUp(){
        // fazer setup
    }

    @Test 
    public void whenSearchValidId_thenCarShouldReturned(){
        Car car1 = new Car("Audi","A3");

        when(carRepository.findByCarId(anyLong())).thenReturn(car1);

        assertThat( carService.getCarDetails(1L)).isEqualTo(car1);

        verify(carRepository, times(1)).findByCarId(anyLong());
    }

    @Test 
    public void whenSearchInvalidId_thenNullShouldBeReturned(){
        when(carRepository.findByCarId(anyLong())).thenReturn(null);

        assertThat( carService.getCarDetails(1L)).isEqualTo(null);

        verify(carRepository, times(1)).findByCarId(anyLong());
    }

    
    @Test 
    public void given3Cars_thenReturn3Records(){
        Car car1 = new Car("Audi","A3");
        Car car2 = new Car("Ford","Fiesta");
        Car car3 = new Car("Opel","Corsa");

        List<Car> AllCars = Arrays.asList(car1,car2,car3);

        when(carRepository.findAll()).thenReturn(AllCars);

        assertThat( carService.getAllCars()).isEqualTo(AllCars);

        verify(carRepository, times(1)).findAll();

    }

}
