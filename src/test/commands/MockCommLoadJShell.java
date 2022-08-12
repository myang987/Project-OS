package test.commands;

import java.util.ArrayList;
import java.util.Stack;

import commands.CommLoadJShell;
import driver.Controller;
import util.Path;
import util.PathInterpreter;

public class MockCommLoadJShell extends CommLoadJShell{

	protected MockCommLoadJShell(Controller controller, ArrayList<String> history, PathInterpreter pathInterpreter,
			Stack<Path> pathStack) {
		super(controller, history, pathInterpreter, pathStack);
	}

}
