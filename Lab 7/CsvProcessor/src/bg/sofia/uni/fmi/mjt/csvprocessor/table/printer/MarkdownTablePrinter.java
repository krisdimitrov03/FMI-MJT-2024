package bg.sofia.uni.fmi.mjt.csvprocessor.table.printer;

import bg.sofia.uni.fmi.mjt.csvprocessor.table.Table;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;

public class MarkdownTablePrinter implements TablePrinter {

    private static final int MIN_COLUMN_LENGTH = 3;

    @Override
    public Collection<String> printTable(Table table, ColumnAlignment... alignments) {
        Collection<String> result = new LinkedHashSet<>();

        int[] lengths = getMaxLengths(table);

        String headersLine = printLine(table.getColumnNames().toArray(new String[0]), lengths);
        result.add(headersLine);

        String alignmentsLine = printAlignments(lengths, alignments);
        result.add(alignmentsLine);

        for (int i = 0; i < table.getRowsCount() - 1; i++) {
            Collection<String> rowCells = new LinkedHashSet<>();

            for (String colName : table.getColumnNames()) {
                String cell = table.getColumnData(colName).toArray()[i].toString();
                rowCells.add(cell);
            }

            String line = printLine(rowCells.toArray(new String[0]), lengths);
            result.add(line);
        }

        return Collections.unmodifiableCollection(result);
    }

    private String printLine(String[] cells, int[] lengths) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < cells.length; i++) {
            builder.append("| ");
            builder.append(cells[i]).repeat(" ", lengths[i] - cells[i].length());
            builder.append(" ");
        }

        builder.append("|");

        return builder.toString();
    }

    private String printAlignments(int[] lengths, ColumnAlignment[] alignments) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < lengths.length; i++) {
            builder.append("| ");

            if (alignments.length > i) {
                printAlignment(alignments[i], builder, lengths[i]);
            } else {
                builder.repeat("-", lengths[i]);
            }

            builder.append(" ");
        }

        builder.append("|");

        return builder.toString();
    }

    private void printAlignment(ColumnAlignment alignment, StringBuilder builder, int lengthOfCol) {
        switch (alignment) {
            case LEFT:
                builder.append(":")
                    .repeat("-", lengthOfCol - 1);
                break;
            case CENTER:
                builder.append(":")
                    .repeat("-", lengthOfCol - 2)
                    .append(":");
                break;
            case RIGHT:
                builder.repeat("-", lengthOfCol - 1)
                    .append(":");
                break;
            case NOALIGNMENT:
                builder.repeat("-", lengthOfCol);
                break;
        }
    }

    private int[] getMaxLengths(Table table) {
        Collection<Integer> lengths = new LinkedList<>();

        for (String colName : table.getColumnNames()) {
            int maxLength = MIN_COLUMN_LENGTH;

            if (colName.length() > maxLength) {
                maxLength = colName.length();
            }

            for (int i = 0; i < table.getRowsCount() - 1; i++) {
                String cell = table.getColumnData(colName).toArray()[i].toString();

                if (cell.length() > maxLength) {
                    maxLength = cell.length();
                }
            }

            lengths.add(maxLength);
        }

        int[] result = new int[lengths.size()];

        int i = 0;
        for (var length : lengths) {
            result[i++] = length;
        }

        return result;
    }
}
