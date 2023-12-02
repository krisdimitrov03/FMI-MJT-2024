package bg.sofia.uni.fmi.mjt.csvprocessor.table.printer;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.BaseTable;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class MarkdownTablePrinterTest {

    private Table table;

    private final String[] headers = {"N", "FirstName", "LastName", "fNumber"};

    private final String[] firstRow = {"1", "George", "Patrick", "93827"};

    private final String[] secondRow = {"2", "Allan", "Peterson", "67453"};

    private TablePrinter printer;

    private final ColumnAlignment[] alignments =
        {ColumnAlignment.NOALIGNMENT, ColumnAlignment.LEFT, ColumnAlignment.CENTER, ColumnAlignment.RIGHT};

    private Collection<String> expectedTable;

    @BeforeEach
    void setup() throws CsvDataNotCorrectException {
        table = new BaseTable();
        table.addData(headers);
        table.addData(firstRow);
        table.addData(secondRow);

        printer = new MarkdownTablePrinter();

        expectedTable = new LinkedHashSet<>();
        expectedTable.addAll(List.of(
            "| N   | FirstName | LastName | fNumber |",
            "| --- | :-------- | :------: | ------: |",
            "| 1   | George    | Patrick  | 93827   |",
            "| 2   | Allan     | Peterson | 67453   |"
        ));
    }

    @Test
    void testPrintTableForCorrectFormat() {
        assertIterableEquals(Collections.unmodifiableCollection(expectedTable), printer.printTable(table, alignments));
    }
}