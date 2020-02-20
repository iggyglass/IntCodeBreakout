// NOTE: Normally I'd write something
// like this with generics, but java
// generics are trash, and not worth
// the effort
public class ArrayStack
{

    private Long[] data;
    private int sp = 0;

    public ArrayStack(int maxStack)
    {
        data = new Long[maxStack];
    }

    public void Push(Long value)
    {
        data[sp] = value;
        sp++;
    }

    public Long Pop()
    {
        sp--;
        return data[sp + 1];
    }

    public Long Peek()
    {
        return data[sp];
    }

    public int GetCount()
    {
        return sp;
    }

    public void Clear()
    {
        sp = 0;
    }

    public Long[] GetArray()
    {
        return data;
    }
}