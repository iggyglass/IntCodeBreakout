import java.util.HashMap;
import java.util.Map;

public class Opcode
{

    public Code Operation;
    public ParameterMode ModeA;
    public ParameterMode ModeB;
    public ParameterMode ModeC;

    public enum ParameterMode
    {
        Position,
        Immediate,
        Relative
    }

    // Java enum syntax is trash
    public enum Code
    {
        Add(1), Multiply(2), Input(3), Output(4), JumpNotZero(5), JumpZero(6), LessThan(7), Equals(8), Relative(9), Halt(99);

        private int value;
        private static Map<Integer, Code> map = new HashMap<>();

        static
        {
            for (Code code : Code.values())
            {
                map.put(code.value, code);
            }
        }

        public int getValue()
        {
            return value;
        }

        public static Code valueOf(int code)
        {
            return (Code)map.get(code);
        }

        private Code(int value)
        {
            this.value = value;
        }
    }

    public Opcode(Code operation)
    {
        Operation = operation;
    }
}