package utils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

/**
 * User: And390
 * Date: 05.09.12
 * Time: 19:58
 */
public class WrapperClassGenerator
{
    public static void generate(Class class_, boolean nonAbstract, Appendable output) throws IOException
    {
        output.append("public class ").append(class_.getSimpleName()).append("Wrapper ").append(class_.isInterface() ? "implements " : "extends ").append(class_.getCanonicalName()).append("\n{\n");
        output.append("    private final ").append(class_.getCanonicalName()).append(" inner;\n");
        output.append("    public ").append(class_.getSimpleName()).append("Wrapper (").append(class_.getCanonicalName()).append(" inner)  {  this.inner=inner;  }\n\n");

        for (Method method : class_.getMethods())
            if (Modifier.isAbstract(method.getModifiers()) &&
                    (method.getModifiers() & (Modifier.PUBLIC | Modifier.PROTECTED)) != 0)
                generateMethod(method, output);

        if (nonAbstract)  for (Method method : class_.getDeclaredMethods())
            if (!method.isSynthetic() && !Modifier.isAbstract(method.getModifiers()) &&
                    (method.getModifiers() & (Modifier.PUBLIC | Modifier.PROTECTED)) != 0)
                generateMethod(method, output);

        output.append("}\n");
    }

    private static void generateMethod(Method method, Appendable output) throws IOException
    {
        output.append("    ");

        //    modifiers
        if (Modifier.isProtected(method.getModifiers()))  output.append("protected ");
        else  output.append("public ");

        //    generics
        Type[] typeparms = method.getTypeParameters();
        if (typeparms.length > 0)  {
            boolean first = true;
            output.append("<");
            for (Type typeparm : typeparms)  {
                if (!first)  output.append(",");
                if (typeparm instanceof Class)  output.append(((Class) typeparm).getName());
                else  output.append(typeparm.toString());
                first = false;
            }
            output.append("> ");
        }

        //    return type
        Type genRetType = method.getGenericReturnType();
        generateTypeName(genRetType, output);
        output.append(" ");

        //    name
        output.append(method.getName());

        //    parameters
        output.append(" (");
        Type[] params = method.getGenericParameterTypes();
        for (int j=0; j<params.length; j++)  {
            generateTypeName(params[j], output);
            output.append(" _").append(Integer.toString(j));
            if (j<params.length-1)  output.append(", ");
        }
        output.append(")");

        //    excceptions
        Type[] exceptions = method.getGenericExceptionTypes();
        if (exceptions.length > 0)  {
            output.append(" throws ");
            for (int k=0; k<exceptions.length; k++)  {
                output.append((exceptions[k] instanceof Class) ?
                        ((Class) exceptions[k]).getName() : exceptions[k].toString());
                if (k<exceptions.length-1)
                    output.append(", ");
            }
        }

        //    body
        output.append("  {  ");
        if (genRetType!=Void.TYPE)  output.append("return ");
        output.append("inner.").append(method.getName()).append("(");
        for (int i=0; i<params.length; i++)  output.append(i==0 ? "_" : ",_").append(Integer.toString(i));
        output.append(");  }\n");
    }

    private static void generateTypeName(Type type, Appendable output) throws IOException
    {
        if (type instanceof Class)
        {
            Class class_ = (Class) type;
            for (int i=0; ; i++)
                if (class_.isArray())  class_ = class_.getComponentType();
                else  {
                    output.append(class_.getName());
                    for (; i!=0; i--)  output.append("[]");
                    return;
                }
        }
        else
            output.append(type.toString());
    }


    public static class Print
    {
        public static void main(String[] args) throws Exception
        {
            generate(Class.forName(args[0]), true, System.out);
        }
    }
}
