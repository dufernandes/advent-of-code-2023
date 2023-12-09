package advent.of.code.year_2023.day_03;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Asterisk implements Data<String> {

  public static final String ASTERISK = "*";

  private final UUID id = UUID.randomUUID();
  private final String value;
  private final int y, x;

  public Asterisk(int y, int x) {
    this.y = y;
    this.x = x;
    this.value = ASTERISK;
  }

  @Override
  public UUID getId() {
    return id;
  }

  @Override
  public DataType getType() {
    return DataType.ASTERISK;
  }

  @Override
  public String getValue() {
    return this.value;
  }

  @Override
  public void setValue(String value) {
    throw new RuntimeException("No value should be set in a Asterisk object");
  }
}
