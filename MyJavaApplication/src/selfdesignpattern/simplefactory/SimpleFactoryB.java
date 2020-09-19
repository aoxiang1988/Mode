package selfdesignpattern.simplefactory;

public class SimpleFactoryB extends SimpleFactoryBase {
    @Override
    public void function() {
        super.function();
        //TODO self function
        System.out.print("SimpleFactoryB function!!!\n");
    }
}
