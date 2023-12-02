package bg.sofia.uni.fmi.mjt.csvprocessor;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.ColumnAlignment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CsvProcessorTest {

    private final String source =
        "N,Fruit,Quantity,Price,Total\n1,Apple,5,1.20,6.00\n2,Banana,3,1.50,4.50\n3,Peach,7,2.40,16.80";

    private final String expectedOutput =
        """
            | N   | Fruit  | Quantity | Price | Total |
            | --- | :----- | :------: | ----: | :---: |
            | 1   | Apple  | 5        | 1.20  | 6.00  |
            | 2   | Banana | 3        | 1.50  | 4.50  |
            | 3   | Peach  | 7        | 2.40  | 16.80 |""";

    private final String expectedOutputWithoutAlignment =
        """
            | N   | Fruit  | Quantity | Price | Total |
            | --- | ------ | -------- | ----- | ----- |
            | 1   | Apple  | 5        | 1.20  | 6.00  |
            | 2   | Banana | 3        | 1.50  | 4.50  |
            | 3   | Peach  | 7        | 2.40  | 16.80 |""";

    private final String incorrectSource =
        "N,Fruit,Quantity,Total\n1,Apple,5,1.20,6.00\n2,1.50,4.50\n3,Peach,7,2.40,16.80";

    private final String duplicatedColumnsSource =
        "N,Fruit,Fruit,Price,Total\n1,Apple,5,1.20,6.00\n2,Banana,3,1.50,4.50\n3,Peach,7,2.40,16.80";

    private final String delimiter = ",";

    private final ColumnAlignment[] alignments =
        {ColumnAlignment.NOALIGNMENT, ColumnAlignment.LEFT, ColumnAlignment.CENTER, ColumnAlignment.RIGHT,
            ColumnAlignment.CENTER};

    private CsvProcessorAPI processor;

    @BeforeEach
    void setup() {
        processor = new CsvProcessor();
    }

    @Test
    void testReadCsvForIncorrectFormat() throws IOException {
        Reader reader = new StringReader(incorrectSource);

        assertThrows(CsvDataNotCorrectException.class, () -> processor.readCsv(reader, delimiter));

        reader.close();
    }

    @Test
    void testReadCsvForDuplicatedColumnNames() throws IOException {
        Reader reader = new StringReader(duplicatedColumnsSource);

        assertThrows(CsvDataNotCorrectException.class, () -> processor.readCsv(reader, delimiter));

        reader.close();
    }

    @Test
    void testReadCsvForCorrectDataRead() throws CsvDataNotCorrectException, IOException {
        Reader reader = new StringReader(source);
        Writer writer = new StringWriter();

        processor.readCsv(reader, delimiter);
        processor.writeTable(writer, alignments);

        assertEquals(expectedOutput, writer.toString());

        reader.close();
        writer.close();
    }

    @Test
    void testWriteTableWithOnlyOneAlignment() throws CsvDataNotCorrectException, IOException {
        Reader reader = new StringReader(source);
        Writer writer = new StringWriter();

        processor.readCsv(reader, delimiter);
        processor.writeTable(writer, ColumnAlignment.NOALIGNMENT);

        assertEquals(expectedOutputWithoutAlignment, writer.toString());

        reader.close();
        writer.close();
    }
}
