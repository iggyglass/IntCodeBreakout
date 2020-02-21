public class CycleController
{

    private int cycles;
    private int initial;

    public CycleController(int cycles) 
    {
        initial = cycles;
        this.cycles = cycles;
    }

    public void AddCycles(int n)
    {
        cycles += n;
    }

    public void Reset()
    {
        cycles = initial;
    }

    public boolean GetCycle()
    {
        if (cycles > 0)
        {
            cycles--;
            return true;
        }

        return false;
    }
}