public abstract class PizzaDecorator implements Pizza{
    protected final Pizza wrapped;


    public PizzaDecorator(Pizza pizza){
        if(pizza == null){
            throw new IllegalArgumentException("Pizza cant be null");
        }

        wrapped = pizza;
    }

    @Override
    public double getCost() {
        return wrapped.getCost();
    }

    @Override
    public String getDescription() {
        return wrapped.getDescription();
    }
}