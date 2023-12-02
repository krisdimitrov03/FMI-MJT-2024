package bg.sofia.uni.fmi.mjt.csvprocessor.table;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.column.BaseColumn;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.column.Column;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.SequencedCollection;
import java.util.Set;

public class BaseTable implements Table {

    private final Set<Column> columns;

    public BaseTable() {
        columns = new LinkedHashSet<>();
    }

    @Override
    public void addData(String[] data) throws CsvDataNotCorrectException {
        if (data == null) {
            throw new IllegalArgumentException("data is null");
        }

        if (!columns.isEmpty() && data.length != columns.size()) {
            throw new CsvDataNotCorrectException(
                "the data is in incorrect format");
        }

        if (columns.isEmpty()) {
            createHeaders(data);
        } else {
            createRow(data);
        }
    }

    @Override
    public Collection<String> getColumnNames() {
        Collection<String> names = new LinkedHashSet<>();

        for (var column : columns) {
            names.add(column.getData().toArray()[0].toString());
        }

        return Collections.unmodifiableCollection(names);
    }

    @Override
    public Collection<String> getColumnData(String column) {
        if (column == null || column.isEmpty() || column.isBlank()) {
            throw new IllegalArgumentException("column is null or blank");
        }

        if (!getColumnNames().contains(column)) {
            throw new IllegalArgumentException("there is no corresponding column with that name in the table");
        }

        SequencedCollection<String> data = new LinkedHashSet<>();
        for (var col : columns) {
            Collection<String> current = col.getData();
            String columnName = current.toArray()[0].toString();

            if (column.equals(columnName)) {
                data.addAll(current);
                data.removeFirst();
                break;
            }
        }

        return Collections.unmodifiableCollection(data);
    }

    @Override
    public int getRowsCount() {
        if (columns.isEmpty()) {
            return 0;
        }

        return getColumnData((String) getColumnNames().toArray()[0]).size() + 1;
    }

    private void createHeaders(String[] headers) throws CsvDataNotCorrectException {
        if (isThereDuplicates(headers)) {
            throw new CsvDataNotCorrectException("duplicated column names");
        }

        for (String header : headers) {
            Set<String> columnData = new LinkedHashSet<>();
            columnData.add(header);

            columns.add(new BaseColumn(columnData));
        }
    }

    private void createRow(String[] columns) {
        int index = 0;

        for (Column column : this.columns) {
            column.addData(columns[index++]);
        }
    }

    private boolean isThereDuplicates(String[] headers) {
        for (String header : headers) {
            int count = 0;

            for (String duplicate : headers) {
                if (header.equals(duplicate)) {
                    count++;
                }
            }

            if (count > 1) {
                return true;
            }
        }

        return false;
    }
}
