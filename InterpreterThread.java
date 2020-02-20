import java.util.function.*;

// TODO: Slow down to make playable -- sync with frame rate using queue

public class InterpreterThread extends Thread
{

    private Interpreter interpreter;
    private boolean killed = false;
    private boolean dead = false;

    public InterpreterThread(long[] code, Function<Long, Void> outputCallback)
    {
        interpreter = new Interpreter(code, ()->GetInput(), outputCallback);
    }

    @Override
    public void run()
    {
        while (!interpreter.IsHalted() && !killed)
        {
            try { interpreter.Step(); } catch (Exception e) {}
        }

        dead = true;
    }

    public void Kill()
    {
        killed = true;
    }

    public boolean IsDead()
    {
        return dead;
    }

    public Long GetInput()
    {
        return 0l;
    }
}