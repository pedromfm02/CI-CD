package tqs.pedro.jenkins;


import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import tqs.pedro.jenkins.Controller.CarController;
import tqs.pedro.jenkins.Entity.Car;
import tqs.pedro.jenkins.Service.CarManagerService;

@WebMvcTest(CarController.class)
public class CarController_WithMockService_C {
    
    @Autowired
    private MockMvc mvc;    //entry point to the web framework

    // inject required beans as "mockeable" objects
    // note that @AutoWire would result in NoSuchBeanDefinitionException
    @MockBean
    private CarManagerService service;

    @Test
    public void whenPostCar_thenCreateCar() throws Exception{
        Car car1 = new Car("Audi", "A3");

        when(service.save(Mockito.any())).thenReturn(car1);

        mvc.perform(
                post("/api/Car").contentType(MediaType.APPLICATION_JSON).content(JsonUtils.toJson(car1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.model", is("A3")));

        verify(service, times(1)).save(Mockito.any());
    }

    @Test
    public void givenManyCars_whenGetCars_thenReturnJsonArray() throws Exception{
        Car car1 = new Car("Audi", "A3");
        Car car2 = new Car("Opel", "Corsa");
        Car car3 = new Car("Ford", "Raptor");

        List<Car> allCars = Arrays.asList(car1, car2, car3);

        when( service.getAllCars()).thenReturn(allCars);

        mvc.perform(
                get("/api/Cars").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].model", is(car1.getModel())))
                .andExpect(jsonPath("$[1].model", is(car2.getModel())))
                .andExpect(jsonPath("$[2].model", is(car3.getModel())));

        verify(service, times(1)).getAllCars();
    }

    @Test
    public void whenGetCarById_thenReturnCar() throws Exception {
        Car car1 = new Car("Audi", "A3");

        when(service.getCarDetails(anyLong())).thenReturn(car1);

        mvc.perform(
            get("/api/Cars/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.model", is(car1.getModel())));

    verify(service, times(1)).getCarDetails(anyLong());
    }

    @Test
    public void whenGetCarByBadId_thenReturnNull() throws Exception {

        when(service.getCarDetails(anyLong())).thenReturn(null);

        mvc.perform(
            get("/api/Cars/2").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());

    verify(service, times(1)).getCarDetails(anyLong());
    }
}
