import java.security.Provider;
import java.security.Security;

/**
 * User: And390
 * Date: 10.02.14
 * Time: 15:54
 */
public class ListSecurityAlgorithms
{
    public static void main(String[] args) throws Exception
    {
        for (Provider provider: Security.getProviders()) {
            System.out.println(provider.getName());
            for (String key: provider.stringPropertyNames())
                System.out.println("\t" + key + "\t" + provider.getProperty(key));
        }
    }
}
