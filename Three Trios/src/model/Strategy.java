package model;

/**
 * Represents the strategies for the AI.
 */
public class Strategy {
  ThreeTriosModel model;

  public Strategy(ThreeTriosModel model) {
    this.model = model;
  }

  public PlayedCard strat1() {
    return model.strategy1();
  }

  public PlayedCard strat2() {
    return model.strategy2();
  }

  public PlayedCard strat3() {
    return model.strategy3();
  }
}
