package utils.jsonparser;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * User: And390
 * Date: 10.10.14
 * Time: 2:42
 */
public class JSONParser
{

    //                --------    rules    --------

    public Rule root;



    //                --------    parsing    --------

    public static final String STRING_IS_NOT_CLOSED = "String is not closed";
    public static final String ILLEGAL_ESCAPE_CHAR = "Illegal escape symbol";
    public static final String ILLEGAL_ESCAPE_HEX = "Illegal escape hex value";
    public static final String UNKNOW_CHAR = "Unknow character";
    public static final String UNKNOW_WORD = "Unknow word";
    public static final String ILLEGAL_NUMBER = "Illegal number";
    public static final String NOT_TERMINATED = "Unexpected token after root element";
    public static final String UNEXPECTED_TERMINATE = "Unexpected end of content";
    public static final String VALUE_NOT_FOUND = "Value expected";
    public static final String OBJECT_NOT_CLOSED = "Unexpected end of JSON object";
    public static final String FIELD_NOT_FOUND = "string as JSON object field name expected";
    public static final String COLON_NOT_FOUND = "':' expected after JSON object field name";
    public static final String FIELD_SEPARATOR_NOT_FOUND = "',' or '}' expected";
    public static final String UNEXPECTED_FIELD = "Unexpected JSON object field";
    public static final String ARRAY_NOT_CLOSED = "Unexpected end of JSON array";
    public static final String ITEM_SEPARATOR_NOT_FOUND = "',' or ']' expected";
    public static final String UNEXPECTED_ITEM = "Unexpected JSON array item";

    public static final String UNEXPECTED_OBJECT = "Unexpected begin of JSON object";
    public static final String UNEXPECTED_OBJECT_END = "Unexpected end of JSON object";
    public static final String UNEXPECTED_ARRAY = "Unexpected begin of JSON array";
    public static final String UNEXPECTED_ARRAY_END = "Unexpected end of JSON array";
    public static final String UNEXPECTED_NULL = "Unexpected JSON null value";
    public static final String UNEXPECTED_BOOLEAN = "Unexpected JSON boolean value";
    public static final String UNEXPECTED_NUMBER = "Unexpected JSON number value";
    public static final String UNEXPECTED_STRING = "Unexpected JSON string value";
    public static final String OBJECT_EXPECTED = "JSON object expected";
    public static final String ARRAY_EXPECTED = "JSON array expected";
    //public static final String NULL_EXPECTED = "JSON null value expected";
    public static final String BOOLEAN_EXPECTED = "JSON boolean value expected";
    public static final String STRING_EXPECTED = "JSON string value expected";
    public static final String NUMBER_EXPECTED = "JSON number value expected";
    public static final String LONG_EXPECTED = "long integer value expected";
    public static final String STRING_LONG_EXPECTED = "JSON string containing Long value expected";
    public static final String NO_FIELD = "No field";
    public static final String DUPLICATE_FIELD = "Duplicate field";

    public final ArrayList<Object> parseStack = new ArrayList<Object> ();  // стэк из String для хранения полей и Integer для хранения индексов

    public final ArrayList<Object> result = new ArrayList<Object> ();  // стэк для хранения результатов

    public void push(Object value)  {  result.add(value);  }
    @SuppressWarnings("unchecked")  public <T> T pop()  {  return (T) result.remove(result.size()-1);  }
    @SuppressWarnings("unchecked")  public <T> T peek()  {  return (T) result.get(result.size()-1);  }


    public void parse(String content) throws IOException, JSONException  {
        parse(new StringReader (content), root);
    }

    public void parse(Reader input) throws IOException, JSONException  {
        if (root==null)  throw new RuntimeException ("Rules are not set for parser");
        parse(input, root);
    }

    public void parse(String content, Rule rule) throws IOException, JSONException  {
        parse(new StringReader (content), rule);
    }

    public void parse(Reader input, Rule rule) throws IOException, JSONException
    {
        if (buffer==null)  buffer = new char [READ_SIZE];
        p = 0;
        p0 = 0;
        end = 0;
        line = 1;
        col = 1;  //TODO почему здесь было 0???
        if (stringBuffer==null)  stringBuffer = new StringBuilder ();
        lastStringToken = null;
        tokenLine = line;
        tokenCol = col;

        parseStack.clear();
        result.clear();

        try  {
            parseValue(input, rule);
            Token token = readToken(input);
            if (token!=TERMINATOR)  throw new JSONException (this, NOT_TERMINATED, token.toString());
        }
        catch (InternalException e)  {
            if (e.getCause() instanceof JSONException)  throw (JSONException)e.getCause();
            if (e.getCause() instanceof IOException)  throw (IOException)e.getCause();
            throw e;
        }
    }

    protected void parseValue(Reader input, Rule rule) throws IOException, JSONException
    {
        Token token = readToken(input);
        parseValue(input, token, rule);
    }

    protected void parseValue(Reader input, Token token, Rule rule) throws IOException, JSONException
    {
        if (token==TERMINATOR)  throw new JSONException (this, UNEXPECTED_TERMINATE);
        else if (token==OBJECT_OPEN)  parseObject(input, rule);
        else if (token==ARRAY_OPEN)  parseArray(input, rule);
        else if (token==NULL)  rule.nullValue(this);
        else if (token==FALSE)  rule.booleanValue(this, false);
        else if (token==TRUE)  rule.booleanValue(this, true);
        else if (token instanceof StringToken)  rule.stringValue(this, (StringToken)token);
        else if (token instanceof NumberToken)  rule.numberValue(this, (NumberToken)token);
        else  throw new JSONException (this, VALUE_NOT_FOUND, ", but found: ", token.toString());
    }

    protected void parseObject(Reader input, Rule rule) throws IOException, JSONException
    {
        int beginLine = tokenLine;
        int beginCol = tokenCol;
        Rule.Data ruleData = rule.objectBegin(this);
        //
        Token token = readToken(input);
        if (token==TERMINATOR)  throw new JSONException (beginLine, beginCol, parseStack, OBJECT_NOT_CLOSED);
        else if (token!=OBJECT_CLOSE)
        for (;;)  {
            //    read field name
            if (!(token instanceof StringToken))  throw new JSONException (this, FIELD_NOT_FOUND, ", but found: ", token.toString());
            String fieldName = token.toString();
            //    read colon
            token = readToken(input);
            if (token!=COLON)  throw new JSONException (this, COLON_NOT_FOUND, ", but found: ", token.toString());
            //    process field name
            Rule child = rule.objectField(this, ruleData, fieldName);
            if (child==null)  throw new JSONException (this, UNEXPECTED_FIELD, fieldName);
            //    parse value
            parseStack.add(fieldName);
            parseValue(input, child);
            parseStack.remove(parseStack.size()-1);
            //    read comma or close bracket
            token = readToken(input);
            if (token==COMMA)  token = readToken(input);
            else if (token==OBJECT_CLOSE)  break;
            else if (token==TERMINATOR)  throw new JSONException (beginLine, beginCol, parseStack, OBJECT_NOT_CLOSED);
            else  throw new JSONException (this, FIELD_SEPARATOR_NOT_FOUND, ", but found: "+token);
        }
        //
        rule.objectEnd(this, ruleData);
    }

    protected void parseArray(Reader input, Rule rule) throws IOException, JSONException
    {
        int beginLine = tokenLine;
        int beginCol = tokenCol;
        Rule.Data ruleData = rule.arrayBegin(this);
        //
        Token token = readToken(input);
        if (token==TERMINATOR)  throw new JSONException (beginLine, beginCol, parseStack, ARRAY_NOT_CLOSED);
        else if (token!=ARRAY_CLOSE)
        for (int index=0; ; index++)  {
            //    process array item
            Rule child = rule.arrayItem(this, ruleData, index);
            if (child==null)  throw new JSONException (this, UNEXPECTED_ITEM, ""+index);
            //    parse value
            parseStack.add(index);
            parseValue(input, token, child);
            parseStack.remove(parseStack.size()-1);
            //    read comma or close bracket
            token = readToken(input);
            if (token==COMMA)  token = readToken(input);
            else if (token==ARRAY_CLOSE)  break;
            else if (token==TERMINATOR)  throw new JSONException (beginLine, beginCol, parseStack, ARRAY_NOT_CLOSED);
            else  throw new JSONException (this, ITEM_SEPARATOR_NOT_FOUND, ", but found: "+token);
        }
        //
        rule.arrayEnd(this, ruleData);
    }

    
    //                --------    lexer    --------
    
    private static final int READ_SIZE = 8096;
    
    private char[] buffer;
    private int p;
    private int p0;
    private int end;
    private int line;
    private int col;
    private StringBuilder stringBuffer;
    private StringToken lastStringToken;

    public int tokenLine;
    public int tokenCol;
    
    protected Token readToken(Reader input) throws IOException, JSONException
    {
        for (;;)
        {
            if (lastStringToken!=null)  {  lastStringToken.pass();  lastStringToken.pass();  lastStringToken = null;  }

            //    read next portion if need
            if (end==-1)  return TERMINATOR;
            if (p==end)  {
                end = input.read(buffer, 0, READ_SIZE);
                if (end==-1)  return TERMINATOR;
                if (end==0)  continue;
                p = 0;
            }
            //    get next char
            col++;
            char c = buffer[p++];
            //    space
            if (c<=' ')  {  if (c=='\n')  {  col=0;  line++;  }  }
            else  {
                tokenCol = col;
                tokenLine = line;
                if (c==',')  return COMMA;
                else if (c==':')  return COLON;
                else if (c=='{')  return OBJECT_OPEN;
                else if (c=='}')  return OBJECT_CLOSE;
                else if (c=='[')  return ARRAY_OPEN;
                else if (c==']')  return ARRAY_CLOSE;
                else if (c=='"')  return lastStringToken = new StringToken(input);
                else if (c>='0' && c<='9' || c=='-')  {
                    //    read number string
                    p0 = p - 1;
                    stringBuffer.setLength(0);
                    for (;;)  {
                        //    read next char
                        int d = readChar(input);
                        if (d==-1)  break;
                        //    check end of word
                        if (!(d>='0' && d<='9' || d=='.' || d>='a' && d<='z' || d>='A' && d<='Z'))  {  p--;  col--;  break;  }
                    }
                    //    parse number string
                    String numberString = stringBuffer.length()==0 ? new String (buffer, p0, p-p0)
                            : stringBuffer.append(buffer, p0, p-p0).toString();
                    try  {  return new NumberToken (numberString, Double.parseDouble(numberString));  }
                    catch (NumberFormatException e)  {  throw new JSONException (this, ILLEGAL_NUMBER, ": ", numberString, e);  }
                }
                else if (c>='a' && c<='z' || c>='A' && c<='Z')  {
                    //    read keyword (null, false or true are valid)
                    char[] match = c=='n' ? NULL_CHARS : c=='f' ? FALSE_CHARS : c=='t' ? TRUE_CHARS : null;
                    p0 = p - 1;
                    stringBuffer.setLength(0);
                    for (int i=1;; i++)  {
                        //    read next char
                        int d = readChar(input);
                        if (d==-1)  break;
                        //    check end of word
                        if (!(d>='a' && d<='z' || d>='A' && d<='Z'))  {  p--;  col--;  break;  }
                        //    check match
                        else if (match!=null && (i>=match.length || match[i]!=d))  match = null;
                    }
                    //    return null, false, true or error
                    if (match==NULL_CHARS)  return NULL;
                    else if (match==FALSE_CHARS)  return FALSE;
                    else if (match==TRUE_CHARS)  return TRUE;
                    else  throw new JSONException (this, UNKNOW_WORD, stringBuffer.append(buffer, p0, p-p0).toString());
                }
                else
                    throw new JSONException (this, UNKNOW_CHAR, ""+c);
            }
        }
    }

    private int readChar(Reader input)
    {
        if (end==-1)  return -1;
        //    if end of last buffer part, accumulate string part and read next buffer part
        if (p==end)  {
            stringBuffer.append(buffer, p0, end-p0);  //сохранить на случай ошибки
            try  {  end = input.read(buffer, 0, READ_SIZE);  }
            catch (IOException e)  {  throw new InternalException (e);  }
            p = 0;
            p0 = 0;
            if (end<=0)  return -1;
        }
        //    get next char
        col++;
        return buffer[p++];
    }

    private final char[] NULL_CHARS = "null".toCharArray();
    private final char[] FALSE_CHARS = "false".toCharArray();
    private final char[] TRUE_CHARS = "true".toCharArray();

//    protected enum TokenType  {
//        OBJECT_OPEN, OBJECT_CLOSE, ARRAY_OPEN, ARRAY_CLOSE, COMMA, COLON, NULL, FALSE, TRUE, STRING, NUMBER;
//    }

    protected static class Token  {
        //public final TokenType type;
        //public Token(TokenType type_)  {  type=type_;  }
    }
    protected static class StaticToken extends Token  {
        public final String string;
        //public StaticToken(TokenType type_, String string_)  {  super(type_);  string=string_;  }
        public StaticToken(String string_)  {  string=string_;  }
        public String toString()  {  return string;  }
    }
//    protected static final Token TERMINATOR = new StaticToken (null, "EOF");
//    protected static final Token OBJECT_OPEN = new StaticToken (TokenType.OBJECT_OPEN, "{");
//    protected static final Token OBJECT_CLOSE = new StaticToken (TokenType.OBJECT_CLOSE, "}");
//    protected static final Token ARRAY_OPEN = new StaticToken (TokenType.ARRAY_OPEN, "[");
//    protected static final Token ARRAY_CLOSE = new StaticToken (TokenType.ARRAY_CLOSE, "]");
//    protected static final Token COMMA = new StaticToken (TokenType.COMMA, ",");
//    protected static final Token COLON = new StaticToken (TokenType.COLON, ":");
//    protected static final Token NULL = new StaticToken (TokenType.COLON, "null");
//    protected static final Token FALSE = new StaticToken (TokenType.COLON, "false");
//    protected static final Token TRUE = new StaticToken (TokenType.COLON, "true");
    protected static final Token TERMINATOR = new StaticToken ("EOF");
    protected static final Token OBJECT_OPEN = new StaticToken ("{");
    protected static final Token OBJECT_CLOSE = new StaticToken ("}");
    protected static final Token ARRAY_OPEN = new StaticToken ("[");
    protected static final Token ARRAY_CLOSE = new StaticToken ("]");
    protected static final Token COMMA = new StaticToken (",");
    protected static final Token COLON = new StaticToken (":");
    protected static final Token NULL = new StaticToken ("null");
    protected static final Token FALSE = new StaticToken ("false");
    protected static final Token TRUE = new StaticToken ("true");

    public class StringToken extends Token
    {
        private String readed = null;
        private Reader input;
        public StringToken(Reader input_)  {  input=input_;  stringBuffer.setLength(0);  }

        @Override
        public String toString()
        {
            if (readed!=null)  return readed;

            p0 = p;
            for (;;)  {
                //    read next char
                int c = readChar(input);
                if (c==-1)  throwInternal(STRING_IS_NOT_CLOSED);
                //    switch char
                if (c=='\"')  {
                    //    append accumulated part of string and end cycle
                    stringBuffer.append(buffer, p0, p-1-p0);
                    break;
                }
                else if (c=='\\')  {
                    //    append accumulated part of string
                    stringBuffer.append(buffer, p0, p-1-p0);
                    p0=p;
                    //    read next char
                    int d = readChar(input);
                    if (d==-1)  throwInternal(STRING_IS_NOT_CLOSED);
                    c = d;
                    //    switch char
                    if (c=='\\' || c=='\"' || c=='/')  stringBuffer.append(c);
                    else if (c=='b')  stringBuffer.append('\\');
                    else if (c=='n')  stringBuffer.append('\n');
                    else if (c=='r')  stringBuffer.append('\r');
                    else if (c=='t')  stringBuffer.append('\t');
                    else if (c=='f')  stringBuffer.append('\f');
                    else if (c=='u')  {
                        int c1 = readChar(input);  if (c1==-1)  throwInternal(STRING_IS_NOT_CLOSED);
                        int c2 = readChar(input);  if (c2==-1)  throwInternal(STRING_IS_NOT_CLOSED);
                        int c3 = readChar(input);  if (c3==-1)  throwInternal(STRING_IS_NOT_CLOSED);
                        int c4 = readChar(input);  if (c4==-1)  throwInternal(STRING_IS_NOT_CLOSED);
                        int h1 = hexDigit((char)c1);
                        int h2 = hexDigit((char)c2);
                        int h3 = hexDigit((char)c3);
                        int h4 = hexDigit((char)c4);
                        if (h1==-1 || h2==-1 || h3==-1 || h4==-1)  throwInternal(ILLEGAL_ESCAPE_CHAR, "\\u"+c1+c2+c3+c4);
                        stringBuffer.append((char)(c1<<24 | c2<<16 | c3<<8 | c4));
                    }
                    else  throwInternal(ILLEGAL_ESCAPE_HEX, "\\"+c);
                    p0 = p;
                }
                else if (c=='\n')  {
                    col = 0;
                    line++;
                }
            }
            //    save and return
            readed = stringBuffer.toString();
            return readed;
        }

        public void pass()
        {
            if (readed!=null || input==null)  return;

            for (;;)  {
                //    read next char
                int c = readChar(input);
                if (c==-1)  throwInternal(STRING_IS_NOT_CLOSED);
                //    switch char
                if (c=='\"')  break;
                else if (c=='\\')  {
                    //    read next char
                    int d = readChar(input);
                    if (d==-1)  throwInternal(STRING_IS_NOT_CLOSED);
                    c = d;
                    //    switch char
                    if (c=='\\' || c=='\"' || c=='/' || c=='b' || c=='n' || c=='r' || c=='t' || c=='f')  ;
                    else if (c=='u')  {
                        int c1 = readChar(input);  if (c1==-1)  throwInternal(STRING_IS_NOT_CLOSED);
                        int c2 = readChar(input);  if (c2==-1)  throwInternal(STRING_IS_NOT_CLOSED);
                        int c3 = readChar(input);  if (c3==-1)  throwInternal(STRING_IS_NOT_CLOSED);
                        int c4 = readChar(input);  if (c4==-1)  throwInternal(STRING_IS_NOT_CLOSED);
                        if (!isHexDigit((char)c1) || !isHexDigit((char)c2) || !isHexDigit((char)c3) || !isHexDigit((char)c4))
                            throwInternal(ILLEGAL_ESCAPE_CHAR, "\\u"+c1+c2+c3+c4);
                    }
                    else  throwInternal(ILLEGAL_ESCAPE_HEX, "\\"+c);
                }
                else if (c=='\n')  {
                    col = 0;
                    line++;
                }
            }

            input = null;  //so, next toString() will cause NullPointerException
        }

        private void throwInternal(String message, String parameter) throws InternalException  {
            throw new InternalException (new JSONException (JSONParser.this, parameter));
        }
        private void throwInternal(String message) throws InternalException  {  throwInternal(message, null);  }
    }

    protected static class NumberToken extends Token
    {
        public String string;
        public double number;
        public NumberToken(String string_, double number_)  {  string=string_;  number=number_;  }
        public String toString()  {  return string;  }
    }

    private static boolean isHexDigit(char c)  {
        return c>='0' && c<='9' || c>='A' && c<='Z' || c>='a' && c<='z';
    }
    private static int hexDigit(char c)  {
        return c>='0' && c<='9' ? c-'0' : c>='A' && c<='Z' ? c-'A'+10 : c>='a' && c<='z' ? c-'a'+10 : -1;
    }


    //                --------    Test    --------

    public static class Test
    {
        public static class PrintRule extends StringRule  {
            @Override
            public void stringValue(JSONParser parser, StringToken data) throws JSONException
            {
                System.out.println(data.toString());
            }
        }

        private static JSONParser parser = new JSONParser ();

        public static void testErr(String content, String code) throws Exception
        {
            try  {  parser.parse(content);  throw new Exception ("No error");  }
            catch (JSONException e)  {  if (e.code!=code)  throw new Exception ("Wrong error code: '"+e.code+"' <> '"+code+"'");  }
        }

        public static void testErr(String content, String code, int line, int col, Object parameter) throws Exception
        {
            try  {  parser.parse(content);  throw new Exception ("No error");  }
            catch (JSONException e)  {
                if (e.code!=code)  throw new Exception ("Wrong error code: '"+e.code+"' <> '"+code+"'", e);
                if (e.line!=line || e.code!=code)  throw new Exception ("Wrong error line:col: "+e.line+":"+e.col+" <> "+line+":"+col, e);
                if (e.parameter==null ? parameter!=null :
                        e.parameter instanceof NumberToken ? !(parameter instanceof Double) || ((NumberToken)e.parameter).number!=(Double)parameter :
                        e.parameter instanceof StringToken ? !e.parameter.toString().equals(parameter) :
                        !e.parameter.equals(parameter))  throw new Exception ("Wrong error parameter: "+e.parameter+" <> "+parameter+"", e);
            }
        }

        public static void testErr(String content, String code, int line, int col) throws Exception  {
            testErr(content, code, line, col, null);
        }

        public static void testSuc(String content) throws IOException, JSONException
        {
            parser.parse(content);
        }

        public static void main(String[] args) throws Exception
        {
            //    TODO можно было бы еще протестировать Lexer - незакрытые строки, неправильные числа, и прочее
            //    default Rule methods implementation must throw errors
            parser.root = new WrapRule ();
            testErr("", UNEXPECTED_TERMINATE, 1, 1);
            testErr("@", UNKNOW_CHAR, 1, 1, "@");
            testErr("a", UNKNOW_WORD, 1, 1, "a");
            testErr("null", UNEXPECTED_NULL, 1, 1);
            testErr(" false", UNEXPECTED_BOOLEAN, 1, 2, false);
            testErr("\ntrue", UNEXPECTED_BOOLEAN, 2, 1, true);
            testErr("  \n 1", UNEXPECTED_NUMBER, 2, 2, 1.0);
            testErr("\"a\"", UNEXPECTED_STRING, 1, 1, "a");
            testErr("{", UNEXPECTED_OBJECT, 1, 1);
            testErr("[", UNEXPECTED_ARRAY, 1, 1);
            testErr("}", VALUE_NOT_FOUND, 1, 1, "}");
            testErr("]", VALUE_NOT_FOUND, 1, 1, "]");
            testErr(" \r\n,", VALUE_NOT_FOUND, 2, 1, ",");

            parser.root = new StringRule ();
            testErr("null", STRING_EXPECTED, 1, 1);
            testSuc("\"a\"");

            parser.root = new ObjectRule()
                .add("a", false, new StringRule ())
                .add("b", false, new ObjectRule()
                    .add("c", false, new StringRule())
                )
            ;
            parser.parse("{}");
            parser.parse("{\"a\":\"xxx\"}");
            //parser.parse("{\"b\":{}}");
            parser.parse("{\"a\":\"xxx\",\"b\":{}}");
            parser.parse("{\"a\":\"xxx\",\"b\":{\"c\":\"sss\"}}");

            //TODO test it
        }
    }
}
