public class PizzaBase implements Pizza {
    private final String name_of_product;
    private final int price_of_product;

    public PizzaBase(String name, int baseCost){
        if(name == null || baseCost <= 0 || name.trim().length() == 0){
            throw new IllegalArgumentException("Name is empty(null) or Cost is less that 0");
        }
        name_of_product = name.trim();
        price_of_product = baseCost;
    }
    @Override
    public double getCost() {
        return price_of_product;
    }

    @Override
    public String getDescription() {
        return name_of_product;
    }




}
