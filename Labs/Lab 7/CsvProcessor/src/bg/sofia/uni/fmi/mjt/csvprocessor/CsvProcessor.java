package bg.sofia.uni.fmi.mjt.csvprocessor;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.BaseTable;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.Table;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.ColumnAlignment;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.MarkdownTablePrinter;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.TablePrinter;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.SequencedCollection;

public class CsvProcessor implements CsvProcessorAPI {

    private final Table table;

    public CsvProcessor() {
        this(new BaseTable());
    }

    public CsvProcessor(Table table) {
        this.table = table;
    }

    @Override
    public void readCsv(Reader reader, String delimiter) throws CsvDataNotCorrectException {
        String[] lines = readAllLines(reader);

        SequencedCollection<String[]> rowsOfCols = new LinkedHashSet<>();

        for (String line : lines) {
            rowsOfCols.add(line.split("\\Q" + delimiter + "\\E"));
        }

        int columnsCount = rowsOfCols.getFirst().length;
        for (String[] row : rowsOfCols) {
            if (row.length != columnsCount) {
                throw new CsvDataNotCorrectException("the CSV data is in wrong format");
            }
        }

        for (String[] row : rowsOfCols) {
            table.addData(row);
        }
    }

    @Override
    public void writeTable(Writer writer, ColumnAlignment... alignments) {
        TablePrinter printer = new MarkdownTablePrinter();

        Collection<String> tableRows = printer.printTable(table, alignments);

        int index = 0;
        for (String row : tableRows) {
            try {
                writer.write(row);

                if (index < tableRows.size() - 1) {
                    writer.write("\n");
                    index++;
                }

            } catch (IOException exception) {
                break;
            }
        }
    }

    private String[] readAllLines(Reader reader) {
        StringBuilder allText = new StringBuilder();

        try {
            int current;

            while ((current = reader.read()) != -1) {
                allText.append((char) current);
            }
        } catch (IOException ex) {
            return new String[0];
        }

        return allText.toString().split("\n");
    }
}
