package bg.sofia.uni.fmi.mjt.csvprocessor.table;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BaseTableTest {

    private Table table;

    private final String[] headers = {"FirstName", "LastName", "fNumber"};

    private final String[] row = {"George", "Patrick", "93827"};

    @BeforeEach
    void setup() {
        table = new BaseTable();
    }

    @Test
    void testAddDataForNullValues() {
        assertThrows(IllegalArgumentException.class, () -> table.addData(null));
    }

    @Test
    void testAddDataForHeaders() throws CsvDataNotCorrectException {
        table.addData(headers);

        Set<String> expected = new LinkedHashSet<>(List.of(headers));

        assertIterableEquals(expected, table.getColumnNames());
    }

    @Test
    void testAddDataForIncorrectFormat() throws CsvDataNotCorrectException {
        table.addData(headers);

        assertThrows(CsvDataNotCorrectException.class, () -> table.addData(new String[] {"George", "82"}));
    }

    @Test
    void testAddDataForCorrectResult() throws CsvDataNotCorrectException {
        table.addData(headers);
        table.addData(row);

        assertIterableEquals(Collections.unmodifiableSet(new LinkedHashSet<>(List.of("George"))),
            table.getColumnData("FirstName"));
        assertIterableEquals(Collections.unmodifiableSet(new LinkedHashSet<>(List.of("Patrick"))),
            table.getColumnData("LastName"));
        assertIterableEquals(Collections.unmodifiableSet(new LinkedHashSet<>(List.of("93827"))),
            table.getColumnData("fNumber"));
    }

    @Test
    void testGetColumnNamesForModifications() throws CsvDataNotCorrectException {
        table.addData(headers);

        assertThrows(UnsupportedOperationException.class, () -> table.getColumnNames().remove("FirstName"));
        assertThrows(UnsupportedOperationException.class, () -> table.getColumnNames().remove("LastName"));
        assertThrows(UnsupportedOperationException.class, () -> table.getColumnNames().remove("fNumber"));
    }

    @Test
    void testGetColumnDataForNullOrBlankValues() {
        assertThrows(IllegalArgumentException.class, () -> table.getColumnData(null));
        assertThrows(IllegalArgumentException.class, () -> table.getColumnData(""));
        assertThrows(IllegalArgumentException.class, () -> table.getColumnData(" "));
    }

    @Test
    void testGetColumnDataForInvalidColumnName() throws CsvDataNotCorrectException {
        table.addData(headers);

        assertThrows(IllegalArgumentException.class, () -> table.getColumnData("column1"));
    }

    @Test
    void testGetColumnDataForCorrectResult() throws CsvDataNotCorrectException {
        table.addData(headers);
        table.addData(row);

        assertIterableEquals(Collections.unmodifiableSet(new LinkedHashSet<>(List.of("George"))),
            table.getColumnData("FirstName"));
        assertIterableEquals(Collections.unmodifiableSet(new LinkedHashSet<>(List.of("Patrick"))),
            table.getColumnData("LastName"));
        assertIterableEquals(Collections.unmodifiableSet(new LinkedHashSet<>(List.of("93827"))),
            table.getColumnData("fNumber"));
    }

    @Test
    void testGetColumnDataForModifications() throws CsvDataNotCorrectException {
        table.addData(headers);
        table.addData(row);

        assertThrows(UnsupportedOperationException.class, () -> table.getColumnData("FirstName").remove("George"));
        assertThrows(UnsupportedOperationException.class, () -> table.getColumnData("LastName").remove("Patrick"));
        assertThrows(UnsupportedOperationException.class, () -> table.getColumnData("fNumber").remove("93827"));
    }

    @Test
    void testGetRowsCountForCorrectResult() throws CsvDataNotCorrectException {
        assertEquals(0, table.getRowsCount());

        table.addData(headers);

        assertEquals(1, table.getRowsCount());

        table.addData(row);

        assertEquals(2, table.getRowsCount());
    }
}
