package utils;

/**
 * And390 - 24.05.2015
 * ����� ����� ��� �������� ������, ������������ ��� ������������ ������ ��� ��������� ���������
 * (���, ��� �� ������� � ����� ����������, �����, ����, ����, �� � �.�.)
 */
public class ExternalException extends Exception
{
    public ExternalException() {}
    public ExternalException(String message)  {  super(message);  }
    public ExternalException(String message, Throwable cause)  {  super(message, cause);  }
    public ExternalException(Throwable cause)  {  super(cause);  }
}
