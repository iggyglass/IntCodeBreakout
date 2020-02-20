import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.function.*;

public class Interpreter
{

    private long[] code = new long[65535];
    private int ip = 0;
    private int relative = 0;
    private boolean isDone = false;

    private Callable<Long> inputCallback;
    private Function<Long, Void> outputCallback;

    private enum ParameterType
    {
        A,
        B,
        C
    }

    public Interpreter(long[] code, Callable<Long> inputCallback, Function<Long, Void> outputCallback)
    {
        for (int i = 0; i < code.length; i++)
        {
            this.code[i] = code[i];
        }

        this.inputCallback = inputCallback;
        this.outputCallback = outputCallback;
    }

    public boolean IsHalted()
    {
        return isDone;
    }

    public void Step() throws Exception
    {
        if (isDone) return;

        Opcode op = getOpcode();

        switch (op.Operation)
        {
            case Add:
                code[getWriteIndex(ParameterType.C, op)] = getValue(ParameterType.A, op) + getValue(ParameterType.B, op);
                ip += 4;
                break;
            case Multiply:
                code[getWriteIndex(ParameterType.C, op)] = getValue(ParameterType.A, op) * getValue(ParameterType.B, op);
                ip += 4;
                break;
            case Input:
                code[getWriteIndex(ParameterType.A, op)] = inputCallback.call();
                ip += 2;
                break;
            case Output:
                outputCallback.apply(getValue(ParameterType.A, op));
                ip += 2;
                break;
            case JumpNotZero:
                if (getValue(ParameterType.A, op) != 0) ip = (int)getValue(ParameterType.B, op);
                else ip += 3;
                break;
            case JumpZero:
                if (getValue(ParameterType.A, op) == 0) ip = (int)getValue(ParameterType.B, op);
                else ip += 3;
                break;
            case LessThan:
                code[getWriteIndex(ParameterType.C, op)] = getValue(ParameterType.A, op) < getValue(ParameterType.B, op) ? 1 : 0;
                ip += 4;
                break;
            case Equals:
                code[getWriteIndex(ParameterType.C, op)] = getValue(ParameterType.A, op) == getValue(ParameterType.B, op) ? 1 : 0;
                ip += 4;
                break;
            case Relative:
                relative += getValue(ParameterType.A, op);
                ip += 2;
                break;
            case Halt:
                isDone = true;
                break;
            default:
                throw new Exception(String.format("Invalid Opcode: %d", op));
        }
    }

    private long getValue(ParameterType type, Opcode op) throws Exception
    {
        switch (type)
        {
            case A:
                if (op.ModeA == Opcode.ParameterMode.Position) return code[(int)code[ip + 1]];
                else if (op.ModeA == Opcode.ParameterMode.Immediate) return code[ip + 1];
                else if (op.ModeA == Opcode.ParameterMode.Relative) return code[(int)(code[ip + 1] + relative)];
                break;
            case B:
                if (op.ModeB == Opcode.ParameterMode.Position) return code[(int)code[ip + 2]];
                else if (op.ModeB == Opcode.ParameterMode.Immediate) return code[ip + 2];
                else if (op.ModeB == Opcode.ParameterMode.Relative) return code[(int)(code[ip + 2] + relative)];
                break;
            case C:
                if (op.ModeC == Opcode.ParameterMode.Position) return code[(int)code[ip + 3]];
                else if (op.ModeC == Opcode.ParameterMode.Immediate) return code[ip + 3];
                else if (op.ModeC == Opcode.ParameterMode.Relative) return code[(int)(code[ip + 3] + relative)];
                break;
        }

        throw new Exception("Unable to parse parameter mode.");
    }

    private int getWriteIndex(ParameterType type, Opcode op)
    {
        if ((type == ParameterType.A && op.ModeA == Opcode.ParameterMode.Relative) ||
                (type == ParameterType.B && op.ModeB == Opcode.ParameterMode.Relative) ||
                (type == ParameterType.C && op.ModeC == Opcode.ParameterMode.Relative)) return (int)(code[ip + type.ordinal() + 1] + relative);
        else return (int)code[ip + type.ordinal() + 1];
    }

    private Opcode getOpcode()
    {
        Integer[] digits = getDigits(code[ip]);

        int val = digits[0] + (digits.length >= 2 ? digits[1] : 0) * 10;
        Opcode code = new Opcode(Opcode.Code.valueOf(val));

        code.ModeA = Opcode.ParameterMode.values()[digits.length >= 3 ? digits[2] : 0];
        code.ModeB = Opcode.ParameterMode.values()[digits.length >= 4 ? digits[3] : 0];
        code.ModeC = Opcode.ParameterMode.values()[digits.length >= 5 ? digits[4] : 0];

        return code;
    }

    private Integer[] getDigits(long number)
    {
        Queue<Integer> digits = new LinkedList<>();

        while (number > 0)
        {
            Integer digit = (int)(number % 10);
            number /= 10;
            digits.add(digit);
        }

        Integer[] data = new Integer[digits.size()];
        digits.toArray(data);

        return data;
    }
}