package selfdesignpattern.simplefactory;

public class SimpleFactoryA extends SimpleFactoryBase {
    @Override
    public void function() {
        super.function();
        //TODO self function
        System.out.print("SimpleFactoryA function!!!\n");
    }
}
