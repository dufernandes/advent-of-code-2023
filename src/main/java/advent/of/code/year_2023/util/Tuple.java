package advent.of.code.year_2023.util;


import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public class Tuple<Y, X, Z> {
  private final Y y;
  private final X x;
  private final Z z;
  public Tuple(Y y, X x, Z z) {
    this.y = y;
    this.x = x;
    this.z = z;
  }
}

