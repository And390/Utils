package utils;

import java.io.*;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;

import javax.tools.*;
import javax.tools.JavaCompiler.CompilationTask;

import utils.iterators.*;


// is not thread safe

public class Compiler
{
    public static class Test
    {
        public static abstract class TestObject
        {
            public abstract String testString();
        }

        public static void main(String[] args) throws Exception
        {
            String code =
                    "public class TestClass1 extends utils.Compiler.Test.TestObject  {\n"+
                    "   public String testString()  {\n"+
                    "      return \"test success\";\n"+
                    "   }\n"+
                    "}";

            Class _class = compile("TestClass1", code);

            TestObject object = (TestObject)  _class.newInstance();
            System.out.println(object.testString());
        }
    }


    public static final String GENERATED_SOURCES_SUFFIX = "(generated)";

    public static Class compile(String name, final String code, DiagnosticListener<JavaFileObject> diagnostic)
    {
        //    create compile task
        CompilationTask compile = JAVA_COMPILER.getTask(null, FILE_MANAGER, diagnostic, null, null,
            Arrays.asList(
                    new SimpleJavaFileObject(URI.create(name.replace('.','/')+".java"), JavaFileObject.Kind.SOURCE)
                    {
                        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
                            return code;
                        }
                        public String toString()  {  //source name in Exception stack traces
                            return uri+GENERATED_SOURCES_SUFFIX;
                        }
                    }
            ));

        //    compile
        if (!compile.call())  return null;

        //    load and return class
        return CLASS_LOADER.defineClass(name, FILE_MANAGER.lastCompiled.toBytes());
    }

    public static Class compile(String name, final String code)  {  return compile(name, code, null);  }


    //                --------    вспомогательные объекты

    public static final JavaCompiler JAVA_COMPILER = ToolProvider.getSystemJavaCompiler();

    public static class CompiledObject extends SimpleJavaFileObject implements NextIterator.Item<CompiledObject>
    {
        public CompiledObject(URI uri, JavaFileObject.Kind kind)  {  super(uri, kind);  }

        private ByteArrayOutputStream out;
        private byte[] data;

        public OutputStream openOutputStream()  {  out = new ByteArrayOutputStream ();  return out;  }
        public byte[] toBytes()  {  data=out.toByteArray();  return data;  }
        public InputStream openInputStream()  {  return new ByteArrayInputStream (data);  }

        //    linked list
        public CompiledObject next = null;
        public CompiledObject getNext()  {  return next;  }
    }

    public static class ComiledFileManager extends ForwardingJavaFileManager<StandardJavaFileManager>
    {
        public CompiledObject lastCompiled = null;

        public ComiledFileManager(DiagnosticListener<? super JavaFileObject> diagnostic)  {  
            super(JAVA_COMPILER.getStandardFileManager(diagnostic, null, null));
        }

        private static URI uri(String _uri)  {
            try  {  return new URI(_uri);  }
            catch (URISyntaxException e)  {  throw new RuntimeException (e);  }
        }

        public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location, final String className,
                                                   JavaFileObject.Kind kind, FileObject sibling) throws IOException
        {
            CompiledObject object = new CompiledObject (uri(className), kind);
            //compiled.add(object);
            object.next = lastCompiled;
            lastCompiled = object;
            return object;
        }
        public Iterable<JavaFileObject> list(JavaFileManager.Location location, String packageName,
                                             Set<JavaFileObject.Kind> kinds, boolean recurse) throws IOException
        {
            Iterator<JavaFileObject> objects = super.list(location, packageName, kinds, recurse).iterator();
            if (!objects.hasNext() && location==StandardLocation.CLASS_PATH && kinds.contains(JavaFileObject.Kind.CLASS))
                objects = loadFromClassLoader(packageName).iterator();

            final String packagePrefix = packageName.length()!=0 ? packageName+"." : null;
            return
                new IteratorIterable<JavaFileObject> (new IteratorJoin<JavaFileObject> (
                    objects,
                    new FilterIterator<CompiledObject> (new NextIterator<CompiledObject>(lastCompiled))
                    {
                        protected boolean accept(CompiledObject item)  {
                            return packagePrefix!=null ?
                                        item.getName().startsWith(packagePrefix) :
                                        item.getName().indexOf(".")==-1;
                        }
                    }
                ));
        }
        public String inferBinaryName(JavaFileManager.Location location, JavaFileObject file)
        {
            //    теперь по€вл€ютс€ наши CompiledObject, их нельз€ слать стандартному обработчику
            if (file instanceof CompiledObject)  return file.getName();
            else if (file instanceof ClassLoaderObject)  return ((ClassLoaderObject)file).name;
            else  return super.inferBinaryName(location, file);
        }
    }
    public final static ComiledFileManager FILE_MANAGER = new ComiledFileManager (null);

    //    умеет объ€вл€ть классы (просто выт€нут protected defineClass)
    public static class CompilerClassLoader extends ClassLoader
    {
        public Class defineClass(String name, byte[] bytes)  {  return defineClass(name, bytes, 0, bytes.length);  }

        public Class<?> loadClass(String name) throws ClassNotFoundException {
            try  {  return loadClass(name, false);  }
            catch (ClassNotFoundException e)  {  return Thread.currentThread().getContextClassLoader().loadClass(name);  }
        }
    }
    public static final CompilerClassLoader CLASS_LOADER = new CompilerClassLoader ();


    //    loadFromClassLoader загружает ClassLoaderObject из класслоадера (текущего потока),
    //    это нужно дл€ веб приложений, так как JavaFileManager не видит их классы и все подключенные библиотеки,
    //    только то, что есть в classpath сервера приложений

    public static class ClassLoaderObject extends SimpleJavaFileObject
    {
        public String name;
        public URL url;

        //uri need just for SimpleJavaFileObject and have no mean and can not starts with "jar:file:/" ("file:" works)
        //url need for open input stream
        public ClassLoaderObject(String name_, URI uri, URL url_) {
            super(uri, Kind.CLASS);
            name=name_;
            url = url_;
        }

        @Override
        public InputStream openInputStream() throws IOException {
            return url.openStream();
        }
    }

    private static final String CLASS_FILE_EXTENSION = ".class";

    //todo возможно стоит кэшировать результаты loadFromClassLoader
    // она вызываетс€ дл€ всех пакетов по линии иерархии при любом упоминании класса в исходнике
    // при этом перерываютс€ все джарники, где есть такие пакеты (например, org.apache значит все пакеты веб-сервера)
    // и в каждом джарнике провер€етс€ каждый файл
    //todo эта фигн€ не работает дл€ приложений в war (каталоги не видит вроде)
    public static List<JavaFileObject> loadFromClassLoader(String packageName) throws IOException
    {
        List<JavaFileObject> result = new ArrayList<JavaFileObject>();

        Enumeration<URL> urlEnumeration = Thread.currentThread().getContextClassLoader().getResources(packageName.replaceAll("\\.", "/"));
        while (urlEnumeration.hasMoreElements()) { // one URL for each jar on the classpath that has the given package
            URL packageFolderURL = urlEnumeration.nextElement();
//            result.addAll(listUnder(packageName, packageFolderURL));

            File directory = new File(packageFolderURL.getFile());
            if (!directory.isDirectory())  // browse jar  //todo test
            {
                try {
                    String jarUri = packageFolderURL.toExternalForm().split("!")[0];

                    JarURLConnection jarConn = (JarURLConnection) packageFolderURL.openConnection();
                    String rootEntryName = jarConn.getEntryName();
                    int rootEnd = rootEntryName.length()+1;

                    Enumeration<JarEntry> entryEnum = jarConn.getJarFile().entries();
                    while (entryEnum.hasMoreElements()) {
                        JarEntry jarEntry = entryEnum.nextElement();
                        String name = jarEntry.getName();
                        if (name.startsWith(rootEntryName) && name.indexOf('/', rootEnd) == -1 && name.endsWith(CLASS_FILE_EXTENSION)) {
                            String binaryName = name.replaceAll("/", ".");
                            binaryName = binaryName.replaceAll(CLASS_FILE_EXTENSION + "$", "");
                            result.add(new ClassLoaderObject(binaryName,
                                    URI.create(jarUri.substring(4) + "!/" + name),
                                    URI.create(jarUri + "!/" + name).toURL()));
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Wasn't able to open " + packageFolderURL + " as a jar file", e);
                }
            }
            else  // browse local .class files - useful for local execution
            {
                File[] childFiles = directory.listFiles();
                for (File childFile : childFiles) {
                    if (childFile.isFile()) {
                        // We only want the .class files.
                        if (childFile.getName().endsWith(CLASS_FILE_EXTENSION)) {
                            String binaryName = packageName + "." + childFile.getName();
                            binaryName = binaryName.replaceAll(CLASS_FILE_EXTENSION + "$", "");
                            result.add(new ClassLoaderObject(binaryName, childFile.toURI(), childFile.toURI().toURL()));
                        }
                    }
                }
            }
            // maybe there can be something else for more involved class loaders
        }

        return result;
    }
}
