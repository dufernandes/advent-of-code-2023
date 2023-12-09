package advent.of.code.year_2023.day_03;


import java.util.UUID;

public class PartNumber implements Data<Integer> {

  private UUID id = UUID.randomUUID();
  private int value;

  @Override
  public UUID getId() {
    return id;
  }

  @Override
  public DataType getType() {
    return DataType.NUMBER;
  }

  @Override
  public Integer getValue() {
    return this.value;
  }

  @Override
  public void setValue(Integer value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof PartNumber
            && id.equals(((PartNumber)obj).id)
            && value == ((PartNumber)obj).value;
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
