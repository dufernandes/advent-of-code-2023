package advent.of.code.year_2023.day03.data;

import java.util.UUID;

public interface Data<T> {
  UUID getId();

  DataType getType();

  T getValue();

  void setValue(T value);
}


