package org.example; /**
 * 
 */
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

public class ClassTaster
{
    public static void functionTest(Class<?> cls, String strMethod, Object... params)
    {
        try
        {
            Method      method;

            //테스트 값을 받은경우
            if ( params.length > 0 )
            {
                method = getMethod( cls, strMethod );
                if ( method != null )
                {
                    method.invoke( cls.newInstance(), params );
                }
            }
            else
            {
                method = getMethod( cls, strMethod );
                Class<?>[] arParams = null;
                if (method != null) {
                    arParams = method.getParameterTypes();
                }
                Object[] arValues = null;
                if (arParams != null) {
                    arValues = new Object[arParams.length];
                }

                if (arParams != null) {
                    for(int iIndex = 0; iIndex < arParams.length; iIndex++)
                    {
                        arValues[iIndex] = getDefaultValue( arParams[iIndex] );
                    }
                }
                for(int iIndex = 0; iIndex < arParams.length; iIndex++)
                {
                    Object param = createParam(arParams[iIndex]);
                    if(param instanceof ICheckValue)
                    {
                        exeDefaultType(cls, method, param, iIndex, arValues);
                    }
                    else
                    {
                        if (method != null) {
                            disply(method, arValues, "user param");
                        }
                        Object result = method.invoke( cls.newInstance(), arValues );
                        System.out.print("result = " + result);
                        System.out.print("\n");
                    }
                }
            }

        }
        catch ( Throwable e )
        {
            System.out.print("e = " + e);
            System.out.print("\n");
            System.out.print("error");
            System.out.print("\n");
        }
    }
    
    public static void disply(Method method, Object[] params, String strCheckValue)
    {
        System.out.print( "=======================================================" );
        System.out.print("\n");
        System.out.print( "Test method [" + method.getName() + "()]" );
        System.out.print("\n");
        System.out.print( "Test value [" + strCheckValue + "()]" );
        System.out.print("\n");
        for(Object param : params)
        {
            System.out.print( "param : " + param );
            System.out.print("\n");
        }
        System.out.print( "=======================================================" );
        System.out.print("\n");
    }
    
    public static void exeDefaultType(Class<?> cls, Method method, Object checkType, int iPosition, Object[] arValues)
    {
        try
        {
            Method[] checkMethod = ICheckValue.class.getMethods();
            for (Method value : checkMethod) {
                arValues[iPosition] = value.invoke(checkType);
                disply(method, arValues, value.getName());
                Object result = method.invoke(cls.newInstance(), arValues);
                System.out.print("result = " + result);
                System.out.print("\n");
            }
        }
        catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | InstantiationException e )
        {
            e.printStackTrace();
        }
    }
    
    public static Object getDefaultValue(Class<?> cls)
    {
        Object param;
        try
        {
            param = createParam(cls);
            if(param instanceof ICheckValue)
            {
                return ((ICheckValue)param).getMiddleValue();
            }
            return cls.newInstance();
        }
        catch (IllegalAccessException | InstantiationException e )
        {
            e.printStackTrace();
        }

        return null;
    }

    public static Object createParam(Class<?> cls)
    {
        switch (cls.getName()) {
            case "java.lang.String":
                return new CheckString();
            case "java.lang.Byte":
            case "byte":
                return new CheckByte();
            case "java.lang.Short":
            case "short":
                return new CheckShort();
            case "java.lang.Long":
            case "long":
                return new CheckLong();
            case "java.lang.Integer":
            case "int":
                return new CheckInteger();
            case "java.lang.Float":
            case "float":
                return new CheckFloat();
            case "java.lang.Double":
            case "double":
                return new CheckDouble();
            case "java.lang.Booleane":
            case "boolean":
                return new CheckBoolean();
            case "java.lang.Character":
            case "char":
                return new CheckCharacter();
            default:
                return cls;
        }
    }
    
    public static Method getMethod(Class<?> cls, String strMethod)
    {
        Method[] arMethod = cls.getMethods();
        for(Method methodTemp : arMethod)
        {
            if(methodTemp.getName().equals( strMethod ))
            {
                return methodTemp;
            }
        }
        return null;
    }
}

class CheckString implements ICheckValue
{
    public String getMaxValue()     { return "aslkjf;alskjf;alskdjf;alskdjfaskldjf;laksjdf;laskjdf;aslkdfja;slkdfja;sldkfja;sldkfjas;ldkfja;sldkfja;sldkfjas;ldkfjasldfka;slkdfa;slkdfjasldkfas;ldkfjas;lkdfjas;ldkfjas;dlkfjsd;lfksa"; }
    public String getMiddleValue()  { return "asdfasdfasdfasdfasfafasdfasdfasdfasdfasdf"; }
    public String getMinValue()     { return "";    }
    public String getNull()         { return null; }
    public String getRandom()       { return getMiddleValue(); }
}

class CheckByte implements ICheckValue
{
    public Byte getMaxValue()     { return Byte.MAX_VALUE; }
    public Byte getMiddleValue()  { return 0; }
    public Byte getMinValue()     { return Byte.MIN_VALUE;    }
    public Byte getNull()         { return -1; }
    public Byte getRandom()       { return (byte)new Random().nextInt( Byte.MAX_VALUE ); }
}

class CheckShort implements ICheckValue
{
    public Short getMaxValue()     { return Short.MAX_VALUE; }
    public Short getMiddleValue()  { return 0; }
    public Short getMinValue()     { return Short.MIN_VALUE;   }
    public Short getNull()         { return -1; }
    public Short getRandom()       { return (short)new Random().nextInt( Short.MAX_VALUE ); }
}

class CheckInteger implements ICheckValue
{
    public Integer getMaxValue()     { return Integer.MAX_VALUE; }
    public Integer getMiddleValue()  { return 0; }
    public Integer getMinValue()     { return Integer.MIN_VALUE; }
    public Integer getNull()         { return -1; }
    public Integer getRandom()       { return new Random().nextInt(); }
}

class CheckLong implements ICheckValue
{
    public Long getMaxValue()     { return Long.MAX_VALUE; }
    public Long getMiddleValue()  { return (long)0; }
    public Long getMinValue()     { return Long.MIN_VALUE;    }
    public Long getNull()         { return (long)-1; }
    public Long getRandom()       { return new Random().nextLong(); }
}

class CheckFloat implements ICheckValue
{
    public Float getMaxValue()     { return Float.MAX_VALUE; }
    public Float getMiddleValue()  { return (float)0; }
    public Float getMinValue()     { return Float.MIN_VALUE;   }
    public Float getNull()         { return (float)-1; }
    public Float getRandom()       { return new Random().nextFloat(); }
}

class CheckDouble implements ICheckValue
{
    public Double getMaxValue()     { return Double.MAX_VALUE; }
    public Double getMiddleValue()  { return Double.MAX_VALUE / 2; }
    public Double getMinValue()     { return Double.MIN_VALUE;  }
    public Double getNull()         { return (double)0; }
    public Double getRandom()       { return new Random().nextDouble(); }
}

class CheckBoolean implements ICheckValue
{
    public Boolean getMaxValue()     { return true; }
    public Boolean getMiddleValue()  { return false; }
    public Boolean getMinValue()     { return false; }
    public Boolean getNull()         { return false; }
    public Boolean getRandom()       { return new Random().nextBoolean(); }
}

class CheckCharacter implements ICheckValue
{
    public Character getMaxValue()     { return Character.MAX_VALUE; }
    public Character getMiddleValue()  { return Character.MAX_VALUE / 2; }
    public Character getMinValue()     { return Character.MIN_VALUE;   }
    public Character getNull()         { return null; }
    public Character getRandom()       { return (char)new Random().nextInt( Character.MAX_VALUE ); }
}

interface ICheckValue
{
    Object getMaxValue();
    Object getMiddleValue();
    Object getMinValue();
    Object getNull();
    Object getRandom();
}
