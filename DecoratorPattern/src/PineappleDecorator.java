public class PineappleDecorator extends PizzaDecorator{
    public PineappleDecorator(Pizza pizza){
        super(pizza);
    }
    @Override
    public double getCost(){
        return super.getCost() + 1.5;
    }

    @Override
    public String getDescription(){
        return super.getDescription() + " + pineapple";
    }


}
