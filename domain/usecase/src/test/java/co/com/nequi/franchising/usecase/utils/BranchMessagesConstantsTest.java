package co.com.nequi.franchising.usecase.utils;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

class BranchMessagesConstantsTest {

    @Test
    void shouldInvokePrivateConstructor() throws Exception {
        Constructor<BranchMessagesConstants> constructor = BranchMessagesConstants.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        BranchMessagesConstants instance = constructor.newInstance();
        assertNotNull(instance);
    }

}