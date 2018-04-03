package de.perdian.flightlog.modules.wizard.services;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.perdian.flightlog.modules.wizard.services.WizardData;
import de.perdian.flightlog.modules.wizard.services.WizardDataFactory;
import de.perdian.flightlog.modules.wizard.services.WizardDataServiceImpl;

public class WizardDataServiceImplTest {

    @Test
    public void lookupFlightData() {

        WizardData data2 = new WizardData();
        WizardDataFactory factory1 = Mockito.mock(WizardDataFactory.class);
        WizardDataFactory factory2 = Mockito.mock(WizardDataFactory.class);
        Mockito.when(factory2.createData(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(data2);
        WizardDataFactory factory3 = Mockito.mock(WizardDataFactory.class);

        WizardDataServiceImpl serviceImpl = new WizardDataServiceImpl();
        serviceImpl.setFactories(Arrays.asList(factory1, factory2, factory3));

        WizardData flightDataResolved = serviceImpl.createData("LH", "123", LocalDate.of(2017, 10, 9));
        Assertions.assertEquals(data2, flightDataResolved);
        Mockito.verify(factory1).createData(Mockito.eq("LH"), Mockito.eq("123"), Mockito.eq(LocalDate.of(2017, 10, 9)));
        Mockito.verify(factory2).createData(Mockito.eq("LH"), Mockito.eq("123"), Mockito.eq(LocalDate.of(2017, 10, 9)));
        Mockito.verifyNoMoreInteractions(factory3);

    }

    @Test
    public void lookupFlightDataNothingFound() {

        WizardDataFactory factory1 = Mockito.mock(WizardDataFactory.class);
        WizardDataFactory factory2 = Mockito.mock(WizardDataFactory.class);

        WizardDataServiceImpl serviceImpl = new WizardDataServiceImpl();
        serviceImpl.setFactories(Arrays.asList(factory1, factory2));

        Assertions.assertNull(serviceImpl.createData("LH", "123", LocalDate.of(2017, 10, 9)));
        Mockito.verify(factory1).createData(Mockito.eq("LH"), Mockito.eq("123"), Mockito.eq(LocalDate.of(2017, 10, 9)));
        Mockito.verify(factory2).createData(Mockito.eq("LH"), Mockito.eq("123"), Mockito.eq(LocalDate.of(2017, 10, 9)));

    }

    @Test
    public void lookupFlightDataExceptionFromFlightDataSource() {

        WizardDataFactory factory1 = Mockito.mock(WizardDataFactory.class);
        Mockito.when(factory1.createData(Mockito.any(), Mockito.any(), Mockito.any())).thenThrow(new IllegalArgumentException());
        WizardDataFactory factory2 = Mockito.mock(WizardDataFactory.class);

        WizardDataServiceImpl serviceImpl = new WizardDataServiceImpl();
        serviceImpl.setFactories(Arrays.asList(factory1, factory2));

        Assertions.assertNull(serviceImpl.createData("LH", "123", LocalDate.of(2017, 10, 9)));
        Mockito.verify(factory1).createData(Mockito.eq("LH"), Mockito.eq("123"), Mockito.eq(LocalDate.of(2017, 10, 9)));
        Mockito.verify(factory2).createData(Mockito.eq("LH"), Mockito.eq("123"), Mockito.eq(LocalDate.of(2017, 10, 9)));

    }

}
