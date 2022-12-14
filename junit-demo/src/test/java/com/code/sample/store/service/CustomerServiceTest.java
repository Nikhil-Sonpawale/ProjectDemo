package com.code.sample.store.service;

import com.code.sample.store.model.Customer;
import com.code.sample.store.model.Item;
import com.code.sample.store.repository.ItemRepository;
import com.code.sample.store.service.external.AddressService;
import com.code.sample.store.service.external.HostService;
import com.code.sample.store.service.tools.StaticService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

public class CustomerServiceTest {

    @Spy
    private AddressService addressService;

    @Mock
    private HostService hostService;

    @InjectMocks
    private CustomerService customerService;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testPLZAddressCombination() {

        Customer customer = new Customer("204", "John Do", "221B Bakerstreet");
        when(addressService.getPLZForCustomer(customer)).thenReturn(47891);

        String address = customerService.getPLZAddressCombination(customer);

        assertThat(address, is("47891_221B Bakerstreet"));
    }


    @Test
    public void testPLZAddressCombinationIncludingHostValue() {


        Customer customer = new Customer("204", "John Do", "224B Bakerstreet");

        doAnswer(new Answer<Customer>() {
            @Override
            public Customer answer(InvocationOnMock invocation) throws Throwable {
                Object originalArgument = (invocation.getArguments())[0];
                Customer returnedValue = (Customer) originalArgument;
                returnedValue.setHostValue("TestHostValue");
                return null ;
            }
        }).when(hostService).expand(any(Customer.class));

        when(addressService.getPLZForCustomer(customer)).thenReturn(47891);
        doNothing().when(addressService).updateExternalSystems(customer);


        String address = customerService.getPLZAddressCombinationIncludingHostValue(customer, true);

        Mockito.verify(addressService, times(1)).updateExternalSystems(any(Customer.class));
        assertThat(address, is("47891_224B Bakerstreet_TestHostValue"));
    }
}



