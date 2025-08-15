
package com.example.shortener;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Base62Test {

    @Test
    void roundTrip() {
        long[] nums = {0, 1, 10, 61, 62, 12345, 999999999L};
        for (long n : nums) {
            String enc = Base62.encode(n);
            long dec = Base62.decode(enc);
            assertEquals(n, dec);
        }
    }
}
