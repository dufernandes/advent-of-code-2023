package advent.of.code.year_2023.day_03.data;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class EmptyData implements Data<Void> {

  private UUID id = UUID.randomUUID();

  @Override
  public UUID getId() {
    return id;
  }

  @Override
  public DataType getType() {
    return DataType.EMPTY;
  }

  @Override
  public Void getValue() {
    return null;
  }

  @Override
  public void setValue(Void value) {
    log.info("value {} cannot be set to the Empty data type with id: {}", value, id);
  }
}
