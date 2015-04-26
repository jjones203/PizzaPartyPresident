package worldfoodgame.model;

/**
 * Home of constants for growing pizza ingredients
 * @author jessica
 */
public interface PizzaCropData
{
  double annualIngredPerPerson = 0.15;  /* in metric tons, equal to 150 kg (just under 1 lb/day)
                                           If 100 people want mushroom pizza, need to grow 15 metric tons
                                           of mushrooms to satisfy their annual need */
  
  double conventionalRate = 1;
  double organicRate = 0.8;             // organic cultivation produces 80% as much as conventional
  double gmoRate = 1.25;                // gmo cultivation produces 125% as much as conventional
  
  
  
}
