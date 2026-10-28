public class OliviesDecorator extends PizzaDecorator{
    public OliviesDecorator(Pizza pizza){
        super(pizza);
    }
    @Override
    public double getCost(){
        return super.getCost() + 1;
    }

    @Override
    public String getDescription(){
        return super.getDescription() + " + olivies";
    }


}

