import java.util.function.*;
import java.util.concurrent.Callable;

public class InterpreterThread extends Thread
{

    private Interpreter interpreter;
    private Callable<Boolean> getCycle;
    private boolean killed = false;
    private boolean dead = false;

    public InterpreterThread(long[] code, Function<Long, Void> outputCallback, Callable<Long> inputCallback, Callable<Boolean> getCycle)
    {
        interpreter = new Interpreter(code, inputCallback, outputCallback);
        this.getCycle = getCycle;
    }

    @Override
    public void run()
    {
        while (!interpreter.IsHalted() && !killed)
        {
            try 
            { 
                if (!getCycle.call()) continue;
                interpreter.Step(); 
            } 
            catch (Exception e) {}
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
}