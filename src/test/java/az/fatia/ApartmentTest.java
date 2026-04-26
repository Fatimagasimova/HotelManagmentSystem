package az.fatia;

import az.fatia.enums.Status;
import az.fatia.model.Apartment;
import az.fatia.repository.ApartmentRepository;
import az.fatia.service.ApartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApartmentTest {

    @Mock
    private ApartmentRepository apartmentRepository;

    @InjectMocks
    private ApartmentService apartmentService;

    private Apartment apartment;

    @BeforeEach
    void setUp() {
        apartment = new Apartment(101, new BigDecimal("150.0"));
        apartment.setStatus(Status.AVAILABLE);
    }

    @Test
    void testSaveApartment() {
        apartmentService.save(apartment);
        verify(apartmentRepository, times(1)).save(apartment);
    }

    @Test
    void testReserveApartmentSuccess() {
        when(apartmentRepository.findById(101)).thenReturn(apartment);

        apartmentService.reserve(101, "Mary");

        assertEquals(Status.RESERVED, apartment.getStatus());
        assertEquals("Mary", apartment.getClientName());
    }

    @Test
    void testReleaseApartmentSuccess() {
        apartment.setStatus(Status.RESERVED);
        apartment.setClientName("Mary");
        when(apartmentRepository.findById(101)).thenReturn(apartment);

        apartmentService.release(101);

        assertEquals(Status.AVAILABLE, apartment.getStatus());
        assertNull(apartment.getClientName());
    }

    @Test
    void testReserveAlreadyReservedApartment() {
        apartment.setStatus(Status.RESERVED);
        when(apartmentRepository.findById(101)).thenReturn(apartment);

        apartmentService.reserve(101, "Jane");

        assertEquals(Status.RESERVED, apartment.getStatus());
    }
}