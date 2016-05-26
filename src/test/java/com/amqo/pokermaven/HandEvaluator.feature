Feature: Evaluación de manos de poker
  Queremos saber lo fuerte que es una mano de poker.

  Scenario Outline: Evaluación de dos manos de poker
    Given un HandEvaluator
    When calculamos la comparacion entre <mano0> y <mano1>
    Then el resultado esperado es <resultado>

    Examples: comparaciones con escaleras de color
      | mano0          | mano1          | resultado  |
      | A♣ K♣ T♣ J♣ Q♣ | Q♣ K♣ T♣ J♣ A♣ | iguales    |
      | A♣ K♣ T♣ J♣ Q♣ | 9♣ K♣ T♣ J♣ Q♣ | mano0      |
      | A♣ 5♣ 4♣ 3♣ 2♣ | 6♣ 5♣ 4♣ 3♣ 2♣ | mano1      |
      | A♥ 2♥ 4♥ 3♥ 5♥ | A♣ 5♣ 4♣ 3♣ 2♣ | iguales    |

    Examples: comparaciones con poker
      | mano0          | mano1          | resultado  |
      | A♣ A♥ A♦ A♠ K♠ | A♣ A♥ A♦ A♠ K♠ | iguales    |
      | A♣ A♥ A♦ A♠ K♠ | A♣ A♥ A♦ A♠ Q♠ | mano0      |
      | A♣ A♥ A♦ A♠ J♠ | A♣ A♥ A♦ A♠ Q♠ | mano1      |
      | T♣ T♥ T♦ T♠ 9♠ | T♣ T♥ T♦ T♠ 9♥ | iguales    |
      | T♣ T♥ T♦ T♠ 9♠ | 9♣ 9♥ 9♦ 9♠ A♥ | mano0      |
      | K♣ K♥ K♦ K♠ 9♠ | Q♣ Q♥ Q♦ Q♠ A♥ | mano0      |
