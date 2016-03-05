package ext.csharp;

public interface ACEventFuncRef<L, T extends EventArgs> {
	void Receive(L o, Object sender, T args);
}
