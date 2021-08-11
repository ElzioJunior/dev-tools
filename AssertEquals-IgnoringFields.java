package foo.bar;

import org.junit.ComparisonFailure;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.argThat;

/**
*
*   HOW TO USE
* 
*   assertEqualsIgnoringFields(expected, actual, "fieldToIgnore1", "fieldToIgnore2");
*
**/
public class TestUtil {

    public static void assertEqualsIgnoringFields(Object expected, Object actual, String... fieldsToIgnore) {
        Arrays.asList(fieldsToIgnore).forEach(field -> {
            ReflectionTestUtils.setField(actual, field, null);
            ReflectionTestUtils.setField(expected, field, null);
        });

        if(!expected.toString().equals(actual.toString())) {
            throw new ComparisonFailure("Objetos não são iguais!", expected.toString(), actual.toString());
        }
    }

}