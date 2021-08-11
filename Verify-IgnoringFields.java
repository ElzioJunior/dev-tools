import java.util.Arrays;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.argThat;

/** 
* WHY TO USE
*
* Sometimes you want to test some objects with verify and your object is mapped to 
* set some time propertie like > myObject.setRegisterDate(LocalDate.now())... 
* and when this happens you neeed to use PowerMock, who can be uncompatible in some 
* next java versions..
*
* You can use a class to inject too, and mock this class, but in my opinion injecting classes to just 
* map something is a bad smell
*
* So this method can verify a callable mock ignoring fields, and the way to do this is setting the
* actual and the expected objects fields to null
**/


/** HOW TO USE
*
*   verify(myMockedClass).myMethod(verifyIgnoringFields(expected, "fieldToIgnore1", "fieldToIgnore2"));
*
**/ 


private <T> T verifyIgnoringFields(T expected, String... fieldsToIgnore) {
    return argThat((T actual) -> {
        Arrays.asList(fieldsToIgnore).forEach(field -> {
            ReflectionTestUtils.setField(actual, field, null);
            ReflectionTestUtils.setField(expected, field, null);
        });

        return expected.toString().equals(actual.toString());
    });
}