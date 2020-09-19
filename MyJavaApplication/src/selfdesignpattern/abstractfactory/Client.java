package selfdesignpattern.abstractfactory;

public class Client {

    private static Client mClient = null;

    /*单例模式*/
    public static Client getClient() {
        if(mClient == null)
            mClient = new Client();
        return mClient;
    }

    AbstractFactory createFactory(int factoryType) {
        if(factoryType == DefaultValue.FACTORY_1)
            return new ConcreateFactory1();
        if(factoryType == DefaultValue.FACTORY_2)
            return new ConcreateFactory2();
        return null;
    }

    public void doProductFunction(int factoryType, int productType) {
        AbstractFactory abstractFactory = createFactory(factoryType);
        AbstractProductA abstractProductA;
        AbstractProductB abstractProductB;
        switch (productType) {
            case DefaultValue.PRODUCT_A_1:
            case DefaultValue.PRODUCT_A_2:
                abstractProductA = abstractFactory.createProductA(productType);
                if(abstractProductA != null)
                    abstractProductA.productAFunction();
                else System.out.print("can not product,check the type!!!\n");
                break;
            case DefaultValue.PRODUCT_B_1:
            case DefaultValue.PRODUCT_B_2:
                abstractProductB = abstractFactory.createProductB(productType);
                if(abstractProductB != null)
                    abstractProductB.productBFunction();
                else System.out.print("can not product,check the type!!!\n");
                break;
        }
    }
}
