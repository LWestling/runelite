package info.sigterm.deob.attributes.code.instructions;

import info.sigterm.deob.attributes.code.Instruction;
import info.sigterm.deob.attributes.code.InstructionType;
import info.sigterm.deob.attributes.code.Instructions;
import info.sigterm.deob.execution.Frame;
import info.sigterm.deob.execution.Stack;

public class IMul extends Instruction
{
	public IMul(Instructions instructions, InstructionType type, int pc)
	{
		super(instructions, type, pc);
	}

	@Override
	public void execute(Frame frame)
	{
		Stack stack = frame.getStack();
		
		Integer two = (Integer) stack.pop();
		Integer one = (Integer) stack.pop();
		
		if (one == null || two == null)
			stack.push(this, 0);
		else
			stack.push(this, one * two);
	}
}
