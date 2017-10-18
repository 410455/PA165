package cz.fi.muni.carshop;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import cz.fi.muni.carshop.exceptions.RequestedCarNotFoundException;
import org.junit.Test;

import org.junit.rules.ExpectedException;
import org.junit.Rule;

import cz.fi.muni.carshop.entities.Car;
import cz.fi.muni.carshop.enums.CarTypes;
import cz.fi.muni.carshop.services.CarShopStorageService;
import cz.fi.muni.carshop.services.CarShopStorageServiceImpl;

public class CarShopStorageServiceTest {

	private CarShopStorageService service = new CarShopStorageServiceImpl();

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test()
	public void testPriceCantBeNegative() {
		// JUnit 4.11
//		thrown.expect(IllegalArgumentException.class);
		// JUnit 4.12
		 thrown.reportMissingExceptionWithMessage("We expect exception on negative price").expect(IllegalArgumentException.class);

		service.addCarToStorage(new Car(Color.BLACK, CarTypes.AUDI, 2016, -1));
	}

	@Test
	public void testGetCar() {
		service.addCarToStorage(new Car(Color.BLACK, CarTypes.AUDI, 2016, 899000));

		assertTrue(service.isCarAvailable(Color.BLACK, CarTypes.AUDI).isPresent());
	}

	@Test
	public void testCarShopStorage_containsTypeForExistingCar() {
		service.addCarToStorage(new Car(Color.BLACK, CarTypes.AUDI, 2016, 899000));
		Map<CarTypes, List<Car>> cars = CarShopStorage.getInstancce().getCars();

		assertThat(cars, hasKey(CarTypes.AUDI));
	}

	// expected to fail with JUnit < 4.11
	@Test
	public void testGetCheaperCars_returnsCorrectResult() {
		service.addCarToStorage(new Car(Color.BLACK, CarTypes.AUDI, 2016, 899000));
		service.addCarToStorage(new Car(Color.BLACK, CarTypes.AUDI, 2016, 889000));
		service.addCarToStorage(new Car(Color.WHITE, CarTypes.AUDI, 2016, 859000));
		service.addCarToStorage(new Car(Color.BLUE, CarTypes.AUDI, 2016, 909000));

		assertThat(service.getCheaperCarsOfSameTypeAndYear(new Car(Color.BLACK, CarTypes.AUDI, 2016, 900000)),
				hasSize(3));

	}

	@Test
	public void testSellingACar() {
		Car car = new Car(Color.RED, CarTypes.BMW, 2016, 1200000);
		service.addCarToStorage(car);
		int sizeAfterAdding = CarShopStorage.getInstancce().getCars().size();
		try {
			service.sellCar(car);
			assertEquals(sizeAfterAdding - 1, CarShopStorage.getInstancce().getCars().size());
		} catch (RequestedCarNotFoundException e) {
			fail();
		}
	}

	@Test
    public void testSellingNonExistentCar_throwsException() throws RequestedCarNotFoundException {
	    thrown.expect(RequestedCarNotFoundException.class);
	    service.sellCar(new Car(Color.BLACK, CarTypes.BMW, 2017, 10));
    }

}
