package co.com.nequi.franchising.usecase.utils;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

class FranchiseMessagesConstantsTest {

    @Test
    void shouldInvokePrivateConstructor() throws Exception {
        Constructor<FranchiseMessagesConstants> constructor = FranchiseMessagesConstants.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        FranchiseMessagesConstants instance = constructor.newInstance();
        assertNotNull(instance);
    }

}