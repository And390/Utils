package utils;

import java.io.IOException;

/**
 * User: And390
 * Date: 26.09.14
 * Time: 3:37
 */
public interface RuntimeAppendable extends Appendable
{
    @Override
    public RuntimeAppendable append(CharSequence csq);

    @Override
    public RuntimeAppendable append(CharSequence csq, int start, int end);

    @Override
    public RuntimeAppendable append(char c);
}
