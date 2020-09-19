package selfdesignpattern.simplefactory;

public abstract class SimpleFactoryBase {

    public static final int TYPE_SIMPLE_FACTORY_A = 1;
    public static final int TYPE_SIMPLE_FACTORY_B = 2;

    public static SimpleFactoryBase createFactory(int type) {
        SimpleFactoryBase example;
        switch (type) {
            case TYPE_SIMPLE_FACTORY_A:
                example = new SimpleFactoryA();
                break;
            case TYPE_SIMPLE_FACTORY_B:
                example = new SimpleFactoryB();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        return example;
    }
    void function() {
        //TODO somethings
    }
}
