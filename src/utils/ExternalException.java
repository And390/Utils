package utils;

/**
 * And390 - 24.05.2015
 * Общий класс для внешниих ошибок, генерируется при неправильных данных или поведении окружения
 * (все, что не связано с самой программой, файлы, базы, сеть, ОС и т.п.)
 */
public class ExternalException extends Exception
{
    public ExternalException() {}
    public ExternalException(String message)  {  super(message);  }
    public ExternalException(String message, Throwable cause)  {  super(message, cause);  }
    public ExternalException(Throwable cause)  {  super(cause);  }
}
