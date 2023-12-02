package bg.sofia.uni.fmi.mjt.csvprocessor.table.column;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class BaseColumnTest {

    @Test
    void testConstructorWithValues() {
        Set<String> values = new LinkedHashSet<>();
        values.add("header");
        values.add("row1");
        values.add("row2");

        Column column = new BaseColumn(values);

        assertIterableEquals(Collections.unmodifiableSet(values), column.getData());
    }

    @Test
    void testAddDataForNullOrEmptyOrBlankValues() {
        Column column = new BaseColumn();

        assertThrows(IllegalArgumentException.class, () -> column.addData(null));
        assertThrows(IllegalArgumentException.class, () -> column.addData(""));
        assertThrows(IllegalArgumentException.class, () -> column.addData(" "));
    }

    @Test
    void testAddDataForCorrectResult() {
        Column column = new BaseColumn();
        column.addData("Name");
        column.addData("George");
        column.addData("Michael");

        Set<String> expected = new LinkedHashSet<>();
        expected.add("Name");
        expected.add("George");
        expected.add("Michael");

        assertIterableEquals(expected, column.getData());
    }

    @Test
    void testGetDataForCorrectResult() {
        Column column = new BaseColumn();

        assertIterableEquals(new LinkedHashSet<>(), column.getData());
    }
}