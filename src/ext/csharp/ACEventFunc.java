package ext.csharp;

public interface ACEventFunc<T extends EventArgs>  {
	void receive(Object sender, T args);
}
