package utils;

/**
 * And390 - 24.05.2015
 * Common program configuration error
 */
public class ConfigException extends ExternalException {
    public ConfigException() {}
    public ConfigException(String message)  {  super(message);  }
    public ConfigException(String message, Throwable cause)  {  super(message, cause);  }
    public ConfigException(Throwable cause)  {  super(cause);  }
}
