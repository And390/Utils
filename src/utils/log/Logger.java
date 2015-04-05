package utils.log;

import java.io.Closeable;

/**
 * And390 - 05.04.15.
 */
public abstract class Logger
{
    //    для переопределения - потомку достаточно реализовать только вывод строки и ошибки
    public abstract void log(String message);
    public abstract void log(Throwable error);

    //    дальше вспомогательные методы

    // наверное, это специфичный вариант только для консольных приложений, и тут нужно Exception заменить на UserException
    //public final void log(Exception error)  {
    //    if (error.getClass()==Exception.class)  log(error.getLocalizedMessage());  else  log((Throwable)error);
    //}

    private ColumnsMaker columnsMaker;
    public synchronized void log(String... messages)  {
        if (columnsMaker==null)  columnsMaker = new ColumnsMaker ();
        for (int i=0; i<messages.length; i++)  if (messages[i]==null)  messages[i] = "null";
        log(columnsMaker.get(messages));
    }

    public void close(Closeable closable)  {
        if (closable!=null)  try  {  closable.close();  }  catch (Exception e)  {  log(e);  }
    }

    //    реализации

    public static class Console extends Logger  {
        public void log(String message)  {  System.out.println(message);  }
        public void log(Throwable error)  {  error.printStackTrace();  }
    }

    // print only errors
    public static class Quiet extends Logger  {
        public final Logger nested;
        public Quiet(Logger nested_)  {  nested=nested_;  }
        public void log(String message)  {}  //ignore
        public void log(String... messages)  {}  //ignore
        public void log(Throwable error)  {  nested.log(error);  }
    }

    //public static class File extends Logger  {
    //    public PrintStream stream;
    //    public File(java.io.File file) throws FileNotFoundException {  stream = new PrintStream (new FileOutputStream(file, true));  }
    //    public File(String filename) throws FileNotFoundException {  this(new java.io.File(filename));  }
    //    public void log(String message)  {  stream.println(message);  }
    //    public void log(Throwable error)  {  error.printStackTrace(stream);  }
    //}

    //public static class Join extends Logger  {
    //    public Logger[] loggers;
    //    public Join(Logger... loggers_)  {  loggers=loggers_;  }
    //    public void log(String message)  {  for (Logger logger : loggers)  logger.log(message);  }
    //    public void log(Throwable error)  {  for (Logger logger : loggers)  logger.log(error);  }
    //}


    //    глобальный логгер по умолчанию в консоль и его тихий вариант
    public static final Console console = new Console ();
    public static final Quiet quiet = new Quiet (console);
}
