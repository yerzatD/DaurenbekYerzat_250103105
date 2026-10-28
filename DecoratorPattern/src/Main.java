public class Main{
    static void main(String[] args) {
        Pizza o1 = buildOrder("Pizza", 5, true, true,true);
        System.out.println(o1.getDescription() + " = " + o1.getCost());


    }

    public static Pizza buildOrder(String name, int baseCost, boolean cheese, boolean olivies, boolean pineappple){
        Pizza pizza = new PizzaBase(name, baseCost);

        if (cheese) {
            pizza = new CheeseDecorator(pizza);
        }
        if (olivies) {
            pizza = new OliviesDecorator(pizza);
        }

        if(pineappple){
            pizza = new PineappleDecorator(pizza);
        }

        return pizza;
}}
